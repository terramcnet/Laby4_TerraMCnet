/*
 * Copyright (c) 2025.
 * Created by Heiko Müller on 5.1.2025.
 * Plugin by MisterCore
 * Created for https://terramc.net
 * Coded with Intellij
 */

package net.terramc.addon.packet;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import org.jetbrains.annotations.NotNull;

public class PlayerDataPacket implements Packet {

    public static final int PACKET_ID = 7;

    private String dataType;
    private int value;

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.dataType = reader.readString();
    this.value = reader.readInt();
  }

  public String dataType() {
    return dataType;
  }

  public int value() {
    return value;
  }

  public static class DataType {
        public static final String COINS = "coins";
        public static final String POINTS = "points";
        public static final String JOIN_COUNT = "join_count";
    }

}
