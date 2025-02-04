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

public class ToggleRankUpdatePacket implements Packet {

    public static final int PACKET_ID = 5;

    private String uuid;
    private boolean status;

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.uuid = reader.readString();
    this.status = reader.readBoolean();
  }

  public String uuid() {
    return uuid;
  }

  public boolean status() {
    return status;
  }

}
