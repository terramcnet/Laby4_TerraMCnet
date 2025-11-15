package net.terramc.addon.terrachat.protocol.packets;

import java.util.UUID;
import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;

public class PacketAddonStatistics extends TerraPacket {

  private String action;
  private UUID uuid;
  private String username;
  private String addonVersion;
  private String minecraftVersion;
  private boolean developmentEnvironment;

  public PacketAddonStatistics(String action, UUID uuid, String username, String addonVersion,
      String minecraftVersion, boolean developmentEnvironment) {
    this.action = action;
    this.uuid = uuid;
    this.username = username;
    this.addonVersion = addonVersion;
    this.minecraftVersion = minecraftVersion;
    this.developmentEnvironment = developmentEnvironment;
  }

  public PacketAddonStatistics() {}

  @Override
  public void read(TerraPacketBuffer packetBuffer) {
    this.action = packetBuffer.readString();
    this.uuid = packetBuffer.readUUID();
    this.username = packetBuffer.readString();
    this.addonVersion = packetBuffer.readString();
    this.minecraftVersion = packetBuffer.readString();
    this.developmentEnvironment = packetBuffer.readBoolean();
  }

  @Override
  public void write(TerraPacketBuffer packetBuffer) {
    packetBuffer.writeString(this.action);
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.username);
    packetBuffer.writeString(this.addonVersion);
    packetBuffer.writeString(this.minecraftVersion);
    packetBuffer.writeBoolean(this.developmentEnvironment);
  }

  @Override
  public void handle(TerraPacketHandler packetHandler) {}

}
