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

public class ToggleRankPacket implements Packet {

    public static final int PACKET_ID = 4;

    private boolean status;

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.status = reader.readBoolean();
  }

  public boolean status() {
    return status;
  }

}
