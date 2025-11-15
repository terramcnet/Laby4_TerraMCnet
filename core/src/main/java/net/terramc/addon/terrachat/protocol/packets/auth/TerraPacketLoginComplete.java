package net.terramc.addon.terrachat.protocol.packets.auth;

import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;

public class TerraPacketLoginComplete extends TerraPacket {

  public TerraPacketLoginComplete() {}

  @Override
  public void read(TerraPacketBuffer packetBuffer) {}

  @Override
  public void write(TerraPacketBuffer packetBuffer) {}

  @Override
  public void handle(TerraPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

}
