package net.terramc.addon.terrachat.protocol.packets.auth;

import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;

public class TerraPacketEncryptionRequest extends TerraPacket {

  private String serverId;
  private byte[] publicKey;

  @Override
  public void read(TerraPacketBuffer packetBuffer) {
    this.serverId = packetBuffer.readString();
    this.publicKey = packetBuffer.readByteArray();
  }

  public TerraPacketEncryptionRequest() {}

  @Override
  public void write(TerraPacketBuffer packetBuffer) {}

  @Override
  public void handle(TerraPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String getServerId() {
    return serverId;
  }

  public byte[] getPublicKey() {
    return publicKey;
  }


}
