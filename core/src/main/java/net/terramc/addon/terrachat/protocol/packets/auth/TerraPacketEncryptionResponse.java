package net.terramc.addon.terrachat.protocol.packets.auth;

import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;

public class TerraPacketEncryptionResponse extends TerraPacket {

  private byte[] sharedSecret;

  public TerraPacketEncryptionResponse(byte[] sharedSecret) {
    this.sharedSecret = sharedSecret;
  }

  public TerraPacketEncryptionResponse() {}

  @Override
  public void read(TerraPacketBuffer packetBuffer) {
    this.sharedSecret = packetBuffer.readByteArray();
  }

  @Override
  public void write(TerraPacketBuffer packetBuffer) {
    packetBuffer.writeByteArray(this.sharedSecret);
  }

  @Override
  public void handle(TerraPacketHandler packetHandler) {}

  public byte[] getSharedSecret() {
    return sharedSecret;
  }

}
