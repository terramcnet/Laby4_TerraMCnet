package net.terramc.addon.terrachat.protocol.packets.auth;

import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;
import java.util.UUID;

public class TerraPacketLogin extends TerraPacket {

  private String username;
  private UUID uuid;

  public TerraPacketLogin(String username, UUID uuid) {
    this.username = username;
    this.uuid = uuid;
  }

  public TerraPacketLogin() {}

  @Override
  public void read(TerraPacketBuffer packetBuffer) {
    username = packetBuffer.readString();
    uuid = packetBuffer.readUUID();
  }

  @Override
  public void write(TerraPacketBuffer packetBuffer) {
    packetBuffer.writeString(username);
    packetBuffer.writeUUID(uuid);
  }

  @Override
  public void handle(TerraPacketHandler packet) {}

}
