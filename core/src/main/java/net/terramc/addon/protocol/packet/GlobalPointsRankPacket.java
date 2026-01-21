/*
 * Copyright (c) 2025.
 * Created by Heiko Müller on 5.1.2025.
 * Plugin by MisterCore
 * Created for https://terramc.net
 * Coded with Intellij
 */

package net.terramc.addon.protocol.packet;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import org.jetbrains.annotations.NotNull;

public class GlobalPointsRankPacket implements Packet {

    public static int PACKET_ID = 2;

    private String globalPointsRank;

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.globalPointsRank = reader.readString();
  }

  public String globalPointsRank() {
      return globalPointsRank;
    }
}
