package net.terramc.core.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.packet.protocol.neo.translation.AbstractLabyMod3PayloadTranslationListener;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.ServerInfoData;
import net.terramc.core.data.AddonData;
import net.terramc.core.data.ServerData;
import java.util.UUID;

public class ServerMessageListener extends AbstractLabyMod3PayloadTranslationListener {

  private  TerraAddon addon;

  public ServerMessageListener(TerraAddon addon) {
    super("TerraMod");
    this.addon = addon;
  }

  @Override
  public byte[] translateIncomingPayload(JsonElement messageContent) {
    if(!messageContent.isJsonObject()) {
      return null;
    }

    JsonObject object = messageContent.getAsJsonObject();

    if(object.has("LOBBY")) {
      if(object.get("LOBBY").getAsBoolean()) {
        AddonData.resetValues();
      }
    }

    if(object.has("gameRank")) {
      AddonData.setGameRank(object.get("gameRank").getAsString());
    }
    if(object.has("globalCoins")) {
      AddonData.setCoins(object.get("globalCoins").getAsInt());
    }
    if(object.has("globalPoints")) {
      AddonData.setPoints(object.get("globalPoints").getAsInt());
    }
    if(object.has("gPointRank")) {
      AddonData.setPointsRank(object.get("gPointRank").getAsString());
    }
    /*if(object.has("addonVersion")) {
      AddonData.checkUpdate(object.get("addonVersion").getAsString());
    }*/
    if(object.has("playerRank")) {
      AddonData.setRank(object.get("playerRank").getAsString());
      this.addon.terraMainActivity.updateStaffActivity();
      //this.addon.terraCloudActivity.reload();
    }
    if(object.has("joinedRound")) {
      AddonData.setInRound(object.get("joinedRound").getAsBoolean());
    }
    if(object.has("playTime")) {
      AddonData.setOnlineTime(object.get("playTime").getAsString().replace("_", " "));
    }
    if(object.has("playerJoins")) {
      AddonData.setJoins(object.get("playerJoins").getAsInt());
    }

    // Staff

    if(object.has("vanish")) {
      AddonData.setVanish(object.get("vanish").getAsBoolean());
    }
    if(object.has("autoVanish")) {
      AddonData.setAutoVanish(object.get("autoVanish").getAsBoolean());
    }
    if(object.has("serverTPS")) {
      ServerData.setServerTps(object.get("serverTPS").getAsString());
    }
    if(object.has("serverCpuUsage")) {
      ServerData.setCpuUsage(object.get("serverCpuUsage").getAsString());
    }
    if(object.has("serverHeapUsage")) {
      ServerData.setRamUsage(object.get("serverHeapUsage").getAsString());
    }
    if(object.has("restartTime")) {
      ServerData.setRestartTime(object.get("restartTime").getAsString());
    }

    if(object.has("serverInfo")) {
      ServerInfoData.Information.loadData(object.get("serverInfo").getAsString());
    }
    if(object.has("serverMaxPlayer")) {
      ServerInfoData.MaxPlayers.data = object.get("serverMaxPlayer").getAsString();
    }

    if(object.has("toggleRank")) {
      AddonData.setRankToggled(object.get("toggleRank").getAsBoolean());
    }
    if(object.has("toggleRank_update")) {
      UUID uuid = UUID.fromString(object.get("toggleRank_update").getAsString().split(";")[0]);
      int status = Integer.parseInt(object.get("toggleRank_update").getAsString().split(";")[1]);
      if(status == 1) {
        AddonData.getToggleRankMap().put(uuid, status);
        AddonData.getNickedMap().put(uuid, status);
      } else {
        AddonData.getToggleRankMap().remove(uuid);
        AddonData.getNickedMap().remove(uuid);
      }
    }

    // Nick-System

    if(object.has("nick_skinCached")) {
      String nickName = object.get("nick_skinCached").getAsString();
      //this.addon.pushNotification("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde aus dem Cache geladen.");
      this.addon.pushNotificationIcon("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde aus dem Cache geladen.",
          Icon.head(nickName).enableHat());
    }

    if(object.has("nick_skinNowCached")) {
      String nickName = object.get("nick_skinNowCached").getAsString();
      //this.addon.pushNotification("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde gespeichert.");
      this.addon.pushNotificationIcon("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde gespeichert.",
          Icon.head(nickName).enableHat());
    }

    if(object.has("nick_skinReCached")) {
      String nickName = object.get("nick_skinReCached").getAsString();
      //this.addon.pushNotification("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde erneut gespeichert.");
      this.addon.pushNotificationIcon("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde erneut gespeichert.",
          Icon.head(nickName).enableHat());
    }

    return new byte[0];
  }

  @Override
  public <T extends Packet> byte[] translateOutgoingPayload(T packet) {
    return new byte[0];
  }
}
