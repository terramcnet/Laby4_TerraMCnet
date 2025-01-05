package net.terramc.addon.packet;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import org.jetbrains.annotations.NotNull;

public class NickCachePacket implements Packet {

  public static final int PACKET_ID = 201;

  private String uuid;
  private String nickname;
  private String cacheType;

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.uuid = reader.readString();
    this.nickname = reader.readString();
    this.cacheType = reader.readString();
  }

  public String uuid() {
    return uuid;
  }

  public String nickname() {
    return nickname;
  }

  public String cacheType() {
    return cacheType;
  }

  public static class CacheType {
    public static final String NOW_CACHED = "now_cached";
    public static final String FROM_CACHE = "from_cache";
    public static final String RE_CACHED = "re_cached";
  }

}
