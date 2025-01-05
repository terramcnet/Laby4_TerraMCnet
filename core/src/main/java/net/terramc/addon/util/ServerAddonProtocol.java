package net.terramc.addon.util;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.serverapi.LabyModProtocolService;
import net.labymod.serverapi.api.packet.Direction;
import net.labymod.serverapi.core.AddonProtocol;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.packet.GameRankPacket;
import net.terramc.addon.packet.GlobalPointsRankPacket;
import net.terramc.addon.packet.NickCachePacket;
import net.terramc.addon.packet.NickCachePacket.CacheType;
import net.terramc.addon.packet.PlayTimePacket;
import net.terramc.addon.packet.PlayerDataPacket;
import net.terramc.addon.packet.PlayerDataPacket.DataType;
import net.terramc.addon.packet.PlayerRankPacket;
import net.terramc.addon.packet.ServerUpdatePacket;
import net.terramc.addon.packet.ToggleRankPacket;
import net.terramc.addon.packet.ToggleRankUpdatePacket;
import java.util.UUID;

public class ServerAddonProtocol {

  private TerraAddon addon;
  private AddonProtocol addonProtocol;

  public ServerAddonProtocol(TerraAddon addon) {
    this.addon = addon;
    LabyModProtocolService protocolService = Laby.references().labyModProtocolService();
    this.addonProtocol = new AddonProtocol(protocolService, "terramc");
    protocolService.registry().registerProtocol(this.addonProtocol);
  }

  public void registerPackets() {

    this.addonProtocol.registerPacket(
        PlayerDataPacket.PACKET_ID,
        PlayerDataPacket.class,
        Direction.CLIENTBOUND,
        (uuid, playerDataPacket) -> {
          if(playerDataPacket.dataType().equals(DataType.COINS)) {
            AddonData.setCoins(playerDataPacket.value());
          }
          if(playerDataPacket.dataType().equals(DataType.POINTS)) {
            AddonData.setPoints(playerDataPacket.value());
          }
          if(playerDataPacket.dataType().equals(DataType.JOIN_COUNT)) {
            AddonData.setJoinCount(playerDataPacket.value());
          }
        }
    );

    this.addonProtocol.registerPacket(
        PlayTimePacket.PACKET_ID,
        PlayTimePacket.class,
        Direction.CLIENTBOUND,
        (uuid, playTimePacket) -> AddonData.setPlayTime(playTimePacket.playTime())
    );

    this.addonProtocol.registerPacket(
        ServerUpdatePacket.PACKET_ID,
        ServerUpdatePacket.class,
        Direction.CLIENTBOUND,
        (uuid, serverUpdatePacket) -> {
          if(serverUpdatePacket.lobby()) {
            AddonData.resetValues();
          }
          AddonData.setInRound(serverUpdatePacket.round());
        }
    );

    this.addonProtocol.registerPacket(
        PlayerRankPacket.PACKET_ID,
        PlayerRankPacket.class,
        Direction.CLIENTBOUND,
        ((uuid, playerRankPacket) -> AddonData.setRank(playerRankPacket.playerRank()))
    );

    this.addonProtocol.registerPacket(
        ToggleRankPacket.PACKET_ID,
        ToggleRankPacket.class,
        Direction.CLIENTBOUND,
        (uuid, toggleRankPacket) -> AddonData.rankToggled(toggleRankPacket.status())
    );

    this.addonProtocol.registerPacket(
        ToggleRankUpdatePacket.PACKET_ID,
        ToggleRankUpdatePacket.class,
        Direction.CLIENTBOUND,
        (uuid, toggleRankUpdatePacket) -> {
          UUID targetUUID = UUID.fromString(toggleRankUpdatePacket.uuid());
          if(toggleRankUpdatePacket.status()) {
            AddonData.getToggleRankMap().put(targetUUID, 1);
            AddonData.getNickedMap().put(targetUUID, 1);
          } else {
            AddonData.getToggleRankMap().remove(targetUUID);
            AddonData.getNickedMap().remove(targetUUID);
          }
        }
    );

    this.addonProtocol.registerPacket(
        GlobalPointsRankPacket.PACKET_ID,
        GlobalPointsRankPacket.class,
        Direction.CLIENTBOUND,
        (uuid, globalPointsRankPacket) -> AddonData.setPointsRank(globalPointsRankPacket.globalPointsRank())
    );

    this.addonProtocol.registerPacket(
        GameRankPacket.PACKET_ID,
        GameRankPacket.class,
        Direction.CLIENTBOUND,
        (uuid, gameRankPacket) -> AddonData.setGameRank(gameRankPacket.gameRank())
    );

    this.addonProtocol.registerPacket(
        NickCachePacket.PACKET_ID,
        NickCachePacket.class,
        Direction.CLIENTBOUND,
        (uuid, nickCachePacket) -> {

          if(nickCachePacket.cacheType().equals(CacheType.NOW_CACHED)) {
            if(!nickCachePacket.uuid().equals("NONE")) {
              this.addon.pushNotificationIcon(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickCachePacket.nickname() + "§8] §7wurde gespeichert."),
                  Icon.head(UUID.fromString(nickCachePacket.uuid())));
            } else {
              this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickCachePacket.nickname() + "§8] §7wurde gespeichert."));
            }
          }

          if(nickCachePacket.cacheType().equals(CacheType.FROM_CACHE)) {
            if(!nickCachePacket.uuid().equals("NONE")) {
              this.addon.pushNotificationIcon(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickCachePacket.nickname() + "§8] §7wurde aus dem Cache geladen."),
                  Icon.head(UUID.fromString(nickCachePacket.uuid())));
            } else {
              this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickCachePacket.nickname() + "§8] §7wurde aus dem Cache geladen."));
            }
          }

          if(nickCachePacket.cacheType().equals(CacheType.RE_CACHED)) {
            if(!nickCachePacket.uuid().equals("NONE")) {
              this.addon.pushNotificationIcon(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickCachePacket.nickname() + "§8] §7wurde erneut gespeichert."),
                  Icon.head(UUID.fromString(nickCachePacket.uuid())));
            } else {
              this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickCachePacket.nickname() + "§8] §7wurde erneut gespeichert."));
            }
          }

        }
    );

  }

}
