package net.terramc.addon.terrachat.protocol.packets;

import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;
import java.util.UUID;

public class PacketPlayerStatus extends TerraPacket {

  private UUID uuid;
  private String userName;
  private String status;
  private String addonVersion;
  private String minecraftVersion;
  private boolean tagHidden;

  public PacketPlayerStatus(UUID uuid, String userName, String status, String addonVersion,
      String minecraftVersion, boolean tagHidden) {
    this.uuid = uuid;
    this.userName = userName;
    this.status = status;
    this.addonVersion = addonVersion;
    this.minecraftVersion = minecraftVersion;
    this.tagHidden = tagHidden;
  }

  public PacketPlayerStatus() {}

  @Override
  public void read(TerraPacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readUUID();
    this.userName = packetBuffer.readString();
    this.status = packetBuffer.readString();
    this.addonVersion = packetBuffer.readString();
    this.minecraftVersion = packetBuffer.readString();
    this.tagHidden = packetBuffer.readBoolean();
  }

  @Override
  public void write(TerraPacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.userName);
    packetBuffer.writeString(this.status);
    packetBuffer.writeString(this.addonVersion);
    packetBuffer.writeString(this.minecraftVersion);
    packetBuffer.writeBoolean(this.tagHidden);
  }

  @Override
  public void handle(TerraPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public UUID uuid() {
    return uuid;
  }

  public String userName() {
    return userName;
  }

  public String status() {
    return status;
  }

  public String addonVersion() {
    return addonVersion;
  }

  public String minecraftVersion() {
    return minecraftVersion;
  }

  public boolean tagHidden() {
    return tagHidden;
  }

}
