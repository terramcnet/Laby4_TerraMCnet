package net.terramc.addon.terrachat.protocol;

public abstract class TerraPacket {

  public abstract void read(TerraPacketBuffer packetBuffer);

  public abstract void write(TerraPacketBuffer packetBuffer);

  public abstract void handle(TerraPacketHandler packetHandler);

}
