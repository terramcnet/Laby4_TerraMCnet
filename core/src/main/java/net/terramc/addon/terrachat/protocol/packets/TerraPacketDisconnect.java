package net.terramc.addon.terrachat.protocol.packets;

import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;

public class TerraPacketDisconnect extends TerraPacket {

  private String reason;

  public TerraPacketDisconnect(String reason) {
    this.reason = reason;
  }

  public TerraPacketDisconnect() {}

  @Override
  public void read(TerraPacketBuffer packetBuffer) {
    this.reason = packetBuffer.readString();
  }

  @Override
  public void write(TerraPacketBuffer packetBuffer) {
    if(this.reason != null) {
      packetBuffer.writeString(this.reason);
    } else {
      packetBuffer.writeString("Client error");
    }
  }

  @Override
  public void handle(TerraPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String reason() {
    return reason;
  }

}
