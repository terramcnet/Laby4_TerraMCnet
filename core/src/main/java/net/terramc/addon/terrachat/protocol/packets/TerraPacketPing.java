package net.terramc.addon.terrachat.protocol.packets;

import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;

public class TerraPacketPing extends TerraPacket {

  @Override
  public void read(TerraPacketBuffer packetBuffer) {}

  @Override
  public void write(TerraPacketBuffer packetBuffer) {}

  @Override
  public void handle(TerraPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

}
