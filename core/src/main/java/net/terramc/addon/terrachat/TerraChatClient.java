package net.terramc.addon.terrachat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.session.Session;
import net.labymod.api.client.session.Session.Type;
import net.labymod.api.client.session.SessionAccessor;
import net.labymod.api.concurrent.ThreadFactoryBuilder;
import net.labymod.api.event.Event;
import net.labymod.api.event.EventBus;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.labymod.api.util.I18n;
import net.labymod.api.util.io.LabyExecutors;
import net.labymod.api.util.time.TimeUtil;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.terrachat.event.TerraChatDisconnectEvent;
import net.terramc.addon.terrachat.event.TerraChatStateUpdateEvent;
import net.terramc.addon.terrachat.protocol.TerraChatProtocol;
import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketDisconnect;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketLogin;
import net.terramc.addon.terrachat.session.TerraSession;

public class TerraChatClient {

  private TerraAddon addon;

  private final String ADDRESS = "terramc.net";
  private final int PORT = 6357;

  private SessionAccessor sessionAccessor;
  private TerraChatProtocol protocol = new TerraChatProtocol();
  private ChatState state;
  private TerraChatSession session = null;
  private TerraChatChannelHandler channelHandler = null;

  private Bootstrap bootstrap;
  private final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).withNameFormat("TerraChatNio#%d").build());
  private final ExecutorService executor = LabyExecutors.newFixedThreadPool(2, "TerraChatExecutor#%d");
  private final ScheduledExecutorService timeoutExecutor = LabyExecutors.newScheduledThreadPool(1, "TerraChatTimeoutExecutor#%d");

  private long timeLastKeepAlive;
  private long timeNextConnect;
  private boolean doNotConnect;
  private long lastConnectTriesReset;
  private String lastDisconnectReason;
  private int failedAuthenticationTries;

  public TerraChatClient(TerraAddon addon, SessionAccessor sessionAccessor, EventBus eventBus) {
    this.addon = addon;
    this.state = ChatState.OFFLINE;
    this.timeNextConnect = TimeUtil.getMillis();
    this.lastConnectTriesReset = 0L;
    this.sessionAccessor = sessionAccessor;
    this.failedAuthenticationTries = 0;

    eventBus.registerListener(this);
  }

  public void prepareAsync() {
    LabyExecutors.executeBackgroundTask(this::prepare);
  }

  private void prepare() {
    this.timeoutExecutor.scheduleWithFixedDelay(() -> {
      try {
        long durationKeepAlive = TimeUtil.getMillis() - this.timeLastKeepAlive;
        long durationConnect = this.timeNextConnect - TimeUtil.getMillis();
        if (this.state != ChatState.OFFLINE && durationKeepAlive > 25000L) {
          this.disconnect(Initiator.CLIENT, I18n.translate("terramc.chat.protocol.disconnect.timeout"), "Timeout");
        }

        if (this.state == ChatState.LOGIN && durationConnect < 0 && this.failedAuthenticationTries < 3) {
          this.sendPacket(new TerraPacketLogin(this.addon.labyAPI().getName(), this.addon.labyAPI().getUniqueId()));
        }

        if (this.state == ChatState.OFFLINE && !this.doNotConnect && durationConnect < 0L) {
          this.connect();
        }

        if (this.lastConnectTriesReset + 300000L < TimeUtil.getMillis()) {
          this.lastConnectTriesReset = TimeUtil.getMillis();
        }
      } catch (Throwable e) {
        e.printStackTrace();
      }

    }, 0L, 5L, TimeUnit.SECONDS);
  }

  public void connect() {
    this.doNotConnect = false;
    this.executor.execute(() -> {
      synchronized(this) {
        if (this.state == ChatState.OFFLINE) {
          this.keepAlive();
          this.updateState(ChatState.LOGIN);
          Session session = this.sessionAccessor.getSession();
          if (session == null) {
            session = new TerraSession("Player", UUID.randomUUID(), null, Type.LEGACY);
          }

          this.session = new TerraChatSession(this, session);
          this.channelHandler = new TerraChatChannelHandler(this, this.session);
          this.lastDisconnectReason = null;
          this.bootstrap = new Bootstrap();
          this.bootstrap.group(this.nioEventLoopGroup);
          this.bootstrap.option(ChannelOption.TCP_NODELAY, true);
          this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
          this.bootstrap.channel(NioSocketChannel.class);
          this.bootstrap.handler(this.channelHandler);
          //ConnectAddressResolver.resolveAddress();

          try {
            this.bootstrap.connect(ADDRESS, PORT).syncUninterruptibly();
            this.sendPacket(new TerraPacketLogin(this.addon.labyAPI().getName(), this.addon.labyAPI().getUniqueId()));
          } catch (Exception e) {
            e.printStackTrace();
            this.updateState(ChatState.OFFLINE);
          }

        }
      }
    });
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent event) {
    if (event.isAnotherAccount()) {
      this.disconnect(Initiator.USER, I18n.translate("terramc.chat.protocol.disconnect.sessionSwitch"), "Session Switch");
      if (event.newSession().isPremium()) {
        this.connect();
      }

    }
  }

  @Subscribe
  public void onMoneyChatDisconnect(TerraChatDisconnectEvent event) {
    if(event.getInitiator() != Initiator.USER) {
      this.addon.pushNotification(
          Component.translatable("terramc.chat.title", TextColor.color(255, 255, 85)),
          Component.translatable("terramc.chat.notification.disconnect", NamedTextColor.GRAY, Component.text(event.getReason()))
      );
    }
  }

  @Subscribe
  public void onNetworkLogin(ServerJoinEvent event) {
    if (!this.isAuthenticated()) {
      this.timeNextConnect = TimeUtil.getMillis() + 10000L;
    }

  }

  public void disconnect(Initiator initiator, String reason, String serverReason) {
    long delay = (long)((double)1000.0F * Math.random() * (double)60.0F);
    this.timeNextConnect = TimeUtil.getMillis() + 10000L + delay;
    if (this.doNotConnect && this.lastDisconnectReason != null) {
      this.lastDisconnectReason = reason;
    }

    if (this.state != ChatState.OFFLINE) {
      if (this.session != null) {
        this.session.dispose();
      }

      this.fireEventSync(new TerraChatDisconnectEvent(this, initiator, I18n.translate(reason)));
      this.updateState(ChatState.OFFLINE);
      this.sendPacket(new TerraPacketDisconnect(serverReason == null ? "Logout" : serverReason), (channel) -> {
        if (channel.isOpen()) {
          channel.close();
        }
      });
      this.session = null;
    }

  }

  public void sendPacket(TerraPacket packet) {
    this.sendPacket(packet, null);
  }

  public void sendPacket(TerraPacket packet, Consumer<SocketChannel> callback) {
    SocketChannel channel = this.getChannel();
    if (channel != null && channel.isActive()) {
      if (channel.eventLoop().inEventLoop()) {
        channel.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        if (callback != null) {
          callback.accept(channel);
        }
      } else {
        channel.eventLoop().execute(() -> {
          channel.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
          if (callback != null) {
            callback.accept(channel);
          }

        });
      }

    }
  }

  public void updateState(ChatState state) {
    synchronized(this) {
      this.state = state;
    }
    this.fireEventSync(new TerraChatStateUpdateEvent(this, this.state));
  }

  public void keepAlive() {
    this.timeLastKeepAlive = TimeUtil.getMillis();
  }

  public void fireEventSync(Event event) {
    this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().eventBus().fire(event));
  }

  public boolean isAuthenticated() {
    return this.state == ChatState.PLAY;
  }

  public boolean isConnectionEstablished() {
    return this.state != ChatState.OFFLINE && this.session != null && this.session.isConnectionEstablished();
  }

  public NioSocketChannel getChannel() {
    return this.channelHandler == null ? null : this.channelHandler.getChannel();
  }

  public TerraChatSession session() {
    return session;
  }

  public TerraChatProtocol protocol() {
    return protocol;
  }

  public TerraAddon addon() {
    return addon;
  }

  public String getLastDisconnectReason() {
    return lastDisconnectReason;
  }

  public void increaseFailedAuthenticationTries() {
    this.failedAuthenticationTries++;
  }

  public void resetFailedAuthenticationTries() {
    this.failedAuthenticationTries = 0;
  }

  public enum ChatState {
    LOGIN(0),
    PLAY(1),
    ALL(2),
    OFFLINE(3);

    private final int id;

    ChatState(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

  }

  public enum Initiator {
    USER,
    CLIENT,
    SERVER
  }

}
