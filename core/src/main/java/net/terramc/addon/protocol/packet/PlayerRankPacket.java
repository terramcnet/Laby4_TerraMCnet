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

public class PlayerRankPacket implements Packet {

    public static final int PACKET_ID = 3;

    private String playerRank;

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.playerRank = reader.readString();
  }

  public String playerRank() {
    return playerRank;
  }

}
