package net.terramc.addon;

import java.util.UUID;
import com.google.gson.Gson;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.GsonUtil;
import net.labymod.api.util.version.SemanticVersion;
import net.terramc.addon.activities.navigation.TerraMainActivity;
import net.terramc.addon.activities.navigation.TerraNavigationElement;
import net.terramc.addon.chat.ChatClient;
import net.terramc.addon.chat.ChatServerListener;
import net.terramc.addon.group.TerraGroupIconTag;
import net.terramc.addon.group.TerraGroupTextTag;
import net.terramc.addon.group.TerraTabListRenderer;
import net.terramc.addon.hudwidget.CoinsHudWidget;
import net.terramc.addon.hudwidget.GameRankHudWidget;
import net.terramc.addon.hudwidget.NickHudWidget;
import net.terramc.addon.hudwidget.PointsHudWidget;
import net.terramc.addon.hudwidget.PointsRankHudWidget;
import net.terramc.addon.hudwidget.game.GoldTimerHudWidget;
import net.terramc.addon.hudwidget.game.IronTimerHudWidget;
import net.terramc.addon.listener.ChatMessageListener;
import net.terramc.addon.listener.KeyListener;
import net.terramc.addon.listener.NetworkListener;
import net.terramc.addon.listener.SessionListener;
import net.terramc.addon.util.ApiUtil;
import net.terramc.addon.util.RankUtil;
import net.terramc.addon.util.ServerAddonProtocol;

@AddonMain
public class TerraAddon extends LabyAddon<TerraConfiguration> {

  public static final HudWidgetCategory TERRA = new HudWidgetCategory("terramc");

  private static TerraAddon instance;

  public TerraMainActivity terraMainActivity;

  private ChatClient chatClient;

  private ApiUtil apiUtil;
  private RankUtil rankUtil;
  private ServerAddonProtocol serverAddonProtocol;

  private boolean connected = false;
  public static String doubleLine = "§7§l§o▎§8§l§o▏ ";
  public static String doubleDots = "§7•§8●";

  private Gson gson;

  @Override
  protected void preConfigurationLoad() {
    Laby.references().revisionRegistry().register(new SimpleRevision("terramc", new SemanticVersion("1.5.0"), "2025-02-04"));
  }

  @Override
  protected void enable() {
    this.registerSettingCategory();

    instance = this;
    this.gson = GsonUtil.DEFAULT_GSON;

    this.terraMainActivity = new TerraMainActivity(this);

    this.rankUtil = new RankUtil();

    this.serverAddonProtocol = new ServerAddonProtocol(this);
    this.serverAddonProtocol.registerPackets();

    this.chatClient = new ChatClient(this);
    this.chatClient.connectStartUp();
    this.registerListener(new ChatServerListener());

    UUID uuid = labyAPI().getUniqueId();
    this.apiUtil = new ApiUtil(this);
    this.apiUtil.postAddonStatistics(this.labyAPI().getUniqueId().toString(), this.labyAPI().getName(), true);
    this.apiUtil.loadPlayerStats(uuid);
    this.apiUtil.loadRankData(uuid);

    this.registerListener(new ChatMessageListener(this));
    this.registerListener(new NetworkListener(this));
    this.registerListener(new SessionListener(this));
    this.registerListener(new KeyListener(this));

    labyAPI().navigationService().register("terramc_main_ui", new TerraNavigationElement(this));

    labyAPI().tagRegistry().registerAfter("labymod_role", "terramc_role", PositionType.ABOVE_NAME, new TerraGroupTextTag(this));
    labyAPI().tagRegistry().register("terramc_role_icon", PositionType.RIGHT_TO_NAME, new TerraGroupIconTag(this));
    Laby.references().badgeRegistry().register("terra_role_tab", net.labymod.api.client.entity.player.badge.PositionType.LEFT_TO_NAME, new TerraTabListRenderer(this));

    labyAPI().hudWidgetRegistry().categoryRegistry().register(TERRA);
    labyAPI().hudWidgetRegistry().register(new NickHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new CoinsHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new PointsHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new GameRankHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new PointsRankHudWidget(this));

    labyAPI().hudWidgetRegistry().register(new IronTimerHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new GoldTimerHudWidget(this));

    this.logger().info("[TerraMCnet] Addon enabled.");

    configuration().nameTagConfiguration.hideOwnTag().visibilitySupplier(() -> rankUtil.isStaff());

    configuration().nameTagConfiguration.hideOwnTag().addChangeListener((type, oldValue, newValue) -> {
      if(!rankUtil.isStaff()) return;
      chatClient.util().sendPlayerStatus(labyAPI().getUniqueId().toString(), labyAPI().getName(), false);
    });

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      this.apiUtil.postAddonStatistics(this.labyAPI().getUniqueId().toString(), this.labyAPI().getName(), false);
      this.chatClient.closeConnection();
    }));

  }

  public void pushNotification(Component title, Component text) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(Icon.texture(ResourceLocation.create("terramc", "textures/icon.png")))
        .type(Type.SYSTEM);
    labyAPI().notificationController().push(builder.build());
  }

  public void pushNotificationIcon(Component title, Component text, Icon icon) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(icon)
        .type(Type.SYSTEM);
    labyAPI().notificationController().push(builder.build());
  }

  @Override
  protected Class<TerraConfiguration> configurationClass() {
    return TerraConfiguration.class;
  }

  public static TerraAddon instance() {
    return instance;
  }

  public Gson gson() {
    return gson;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  public ApiUtil apiUtil() {
    return apiUtil;
  }

  public RankUtil rankUtil() {
    return rankUtil;
  }

  public ServerAddonProtocol serverAddonProtocol() {
    return serverAddonProtocol;
  }

  public ChatClient chatClient() {
    return chatClient;
  }

}
