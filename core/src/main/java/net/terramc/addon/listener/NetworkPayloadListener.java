package net.terramc.addon.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.serverapi.protocol.payload.exception.PayloadReaderException;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import java.util.UUID;

public class NetworkPayloadListener {

  private final TerraAddon addon;

  public NetworkPayloadListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onReceive(NetworkPayloadEvent event) {
    if(event.identifier().getNamespace().equals("labymod3") & event.identifier().getPath().equals("main")) {
      try {
        PayloadReader reader = new PayloadReader(event.getPayload());
        String messageKey = reader.readString();
        String messageContent = reader.readString();

        if(messageKey.equals("TerraMod") & this.addon.isConnected()) {
          JsonElement serverMessage = this.addon.gson().fromJson(messageContent, JsonElement.class);
          if(!serverMessage.isJsonObject()) return;
          JsonObject object = serverMessage.getAsJsonObject();

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
          if(object.has("playerRank")) {
            AddonData.setRank(object.get("playerRank").getAsString());
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

          if(object.has("toggleRank")) {
            AddonData.rankToggled(object.get("toggleRank").getAsBoolean());
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
            String[] raw = object.get("nick_skinCached").getAsString().split(";");
            String nickName = raw[0];
            String uuid = raw[1];
            //this.addon.pushNotification("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde aus dem Cache geladen.");
            if(!uuid.equals("NONE")) {
              this.addon.pushNotificationIcon(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickName + "§8] §7wurde aus dem Cache geladen."),
                  Icon.head(UUID.fromString(uuid)).enableHat());
            } else {
              this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickName + "§8] §7wurde aus dem Cache geladen."));
            }
          }

          if(object.has("nick_skinNowCached")) {
            String[] raw = object.get("nick_skinNowCached").getAsString().split(";");
            String nickName = raw[0];
            String uuid = raw[1];
            //this.addon.pushNotification("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde gespeichert.");
            if(!uuid.equals("NONE")) {
              this.addon.pushNotificationIcon(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickName + "§8] §7wurde gespeichert."),
                  Icon.head(UUID.fromString(uuid)).enableHat());
            } else {
              this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickName + "§8] §7wurde gespeichert."));
            }
          }

          if(object.has("nick_skinReCached")) {
            String[] raw = object.get("nick_skinReCached").getAsString().split(";");
            String nickName = raw[0];
            String uuid = raw[1];
            //this.addon.pushNotification("§7§l§o▎§8§l§o▏ §eNick-System", "§7Skin §8[§e" + nickName + "§8] §7wurde erneut gespeichert.");
            if(!uuid.equals("NONE")) {
              this.addon.pushNotificationIcon(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickName + "§8] §7wurde erneut gespeichert."),
                  Icon.head(UUID.fromString(uuid)).enableHat());
            } else {
              this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §eNick-System"), Component.text("§7Skin §8[§e" + nickName + "§8] §7wurde erneut gespeichert."));
            }
          }

        }
      } catch (PayloadReaderException ignored) {

      }
    }
  }

}
