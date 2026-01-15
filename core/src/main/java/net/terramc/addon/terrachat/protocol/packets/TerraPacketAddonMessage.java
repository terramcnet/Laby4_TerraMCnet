package net.terramc.addon.terrachat.protocol.packets;

import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TerraPacketAddonMessage extends TerraPacket {

  private String key;
  private byte[] data;

  public TerraPacketAddonMessage(String key, byte[] data) {
    this.key = key;
    this.data = data;
  }

  public TerraPacketAddonMessage() {
  }

  @Override
  public void read(TerraPacketBuffer packetBuffer) {
    this.key = packetBuffer.readString();
    byte[] data = new byte[packetBuffer.readInt()];
    packetBuffer.readBytes(data);
    this.data = data;
  }

  @Override
  public void write(TerraPacketBuffer packetBuffer) {
    packetBuffer.writeString(this.key);
    packetBuffer.writeInt(this.data.length);
    packetBuffer.writeBytes(this.data);
  }

  @Override
  public void handle(TerraPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String getKey() {
    return key;
  }

  public String getJson() {
    try {
      StringBuilder outStr = new StringBuilder();
      if (this.data != null && this.data.length != 0) {
        if (this.isCompressed(this.data)) {
          try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(this.data));
              BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
              outStr.append(line);
            }
          }
        } else {
          outStr.append(Arrays.toString(this.data));
        }

        return outStr.toString();
      } else {
        return "";
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private byte[] toBytes(String in) {
    byte[] str = in.getBytes(StandardCharsets.UTF_8);

    try {
      if (str.length == 0) {
        return new byte[0];
      } else {
        try (ByteArrayOutputStream obj = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(obj)) {
          gzip.write(str);
          gzip.finish();
          return obj.toByteArray();
        }
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private boolean isCompressed(byte[] compressed) {
    return compressed[0] == 31 && compressed[1] == -117;
  }

}
