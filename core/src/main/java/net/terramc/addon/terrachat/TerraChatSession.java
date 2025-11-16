package net.terramc.addon.terrachat;

import io.netty.channel.socket.nio.NioSocketChannel;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.labymod.api.Laby;
import net.labymod.api.client.session.MinecraftAuthenticator;
import net.labymod.api.client.session.Session;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.terrachat.TerraChatClient.ChatState;
import net.terramc.addon.terrachat.TerraChatClient.Initiator;
import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;
import net.terramc.addon.terrachat.protocol.packets.PacketAddonStatistics;
import net.terramc.addon.terrachat.protocol.packets.PacketPlayerStatus;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketAddonMessage;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketDisconnect;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketPing;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketPong;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketEncryptionRequest;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketEncryptionResponse;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketLoginComplete;
import net.terramc.addon.terrachat.util.CryptManager;
import net.terramc.addon.util.TerraChatUser;

public class TerraChatSession extends TerraPacketHandler {

  private final TerraChatClient chatClient;

  private Session session;
  private boolean premium;
  private boolean connectionEstablished;
  private boolean authenticated;

  public TerraChatSession(TerraChatClient moneyChatClient, Session session) {
    this.chatClient = moneyChatClient;
    this.session = session;
    this.premium = session.isPremium();
    this.connectionEstablished = false;
    this.authenticated = false;
  }

  protected void handlePacket(TerraPacket packet) {
    this.connectionEstablished = true;
    super.handlePacket(packet);
    this.chatClient.keepAlive();
  }

  public void handle(TerraPacketEncryptionRequest encryptionRequest) {
    try {
      PublicKey publicKey = CryptManager.decodePublicKey(encryptionRequest.getPublicKey());
      SecretKey secretKey = CryptManager.createNewSharedKey();
      String serverId = encryptionRequest.getServerId();
      MinecraftAuthenticator authenticator = this.chatClient.addon().labyAPI().minecraft().authenticator();

      String hash = CryptManager.getServerIdHash(serverId, publicKey, secretKey);
      NioSocketChannel nio = this.chatClient.getChannel();
      authenticator.joinServer(this.session, hash).exceptionally((throwable) -> false).thenAccept((result) -> {
        if (this.chatClient.getChannel() == nio) {
          this.chatClient.sendPacket(new TerraPacketEncryptionResponse(secretKey.getEncoded()));
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      this.chatClient.disconnect(Initiator.CLIENT, e.getMessage(), "Client Error - EncryptionRequest");
    }

  }

  @Override
  public void handle(TerraPacketPing packet) {
    this.chatClient.sendPacket(new TerraPacketPong());
    this.chatClient.keepAlive();
  }

  public void handle(TerraPacketLoginComplete packet) {
    this.chatClient.updateState(ChatState.PLAY);
    this.authenticated = true;
    this.chatClient.keepAlive();
    this.chatClient.resetFailedAuthenticationTries();

    this.chatClient.sendPacket(new PacketAddonStatistics("add", this.session.getUniqueId(), this.session.getUsername(),
        this.chatClient.addon().addonInfo().getVersion(), this.chatClient.addon().labyAPI().minecraft().getVersion(), this.chatClient.addon().labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));

    this.chatClient.sendPacket(new PacketPlayerStatus(Laby.labyAPI().getUniqueId(), Laby.labyAPI().getName(),
        "ONLINE", this.chatClient.addon().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(),
        this.chatClient.addon().configuration().nameTagConfiguration.hideOwnTag().get()));
  }

  @Override
  public void handle(PacketPlayerStatus packet) {
    AddonData.getChatUsers().put(packet.uuid(), new TerraChatUser(
       packet.uuid(),
        packet.userName(),
        packet.status(),
        packet.addonVersion(),
        packet.minecraftVersion(),
        packet.tagHidden()
    ));
  }

  @Override
  public void handle(TerraPacketDisconnect packet) {
    this.chatClient.disconnect(Initiator.SERVER, packet.reason(), packet.reason());
  }

  @Override
  public void handle(TerraPacketAddonMessage packet) {
    if(packet.getKey().equals("unauthenticated")) {
      this.resetAuthentication();
    }
  }

  public void dispose() {
    this.connectionEstablished = false;
  }

  private void resetAuthentication() {
    this.authenticated = false;
    this.premium = false;
    this.chatClient.updateState(ChatState.LOGIN);
    this.chatClient.increaseFailedAuthenticationTries();
    //this.labyConnect.fireEventSync(new LabyConnectRejectAuthenticationEvent(this.labyConnect));
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public boolean isPremium() {
    return premium;
  }

  public boolean isConnectionEstablished() {
    return connectionEstablished;
  }

}
