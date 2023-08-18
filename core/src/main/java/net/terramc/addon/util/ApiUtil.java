package net.terramc.addon.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.io.web.request.Request;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.data.ServerInfoData;
import net.terramc.addon.group.StaffGroup;

public class ApiUtil {

  private TerraAddon addon;

  public ApiUtil(TerraAddon addon) {
    this.addon = addon;
  }

  private static String baseUrl = "http://api.terramc.net/";

  // actions = [restart, maintenance]
  public void sendCloudControl(UUID uuid, String action, String group) {
    if(!this.addon.rankUtil().canControlCloud()) return;

    Request.ofString()
        .url("http://45.88.108.143:3610/cloud?uuid="+uuid+"&action="+action+"&group="+group, new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            this.addon.pushNotification(Component.text("§4TMC-Proxy Fehler"), Component.text("§cResponse-Code '" + response.getStatusCode() + "' von Proxy bekommen."));
            return;
          }
          String result = response.get();
          if(result.startsWith("200")) {
            this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §aCloud-Steuerung"), Component.text("§aErfolgreich §8[§e" + result + "§8]"));
          } else {
            this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §aCloud-Steuerung"), Component.text("§cFehler §8[§e" + result + "§8]"));
          }
        });
  }

  public void loadServerData(UUID uuid) {
    Request.ofGson(JsonObject.class)
        .url(baseUrl + "staff?req=serverData&uuid="+uuid, new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            if(response.getStatusCode() == 401) return;
            this.addon.pushNotification(Component.translatable("terramc.notification.error.title"), Component.translatable("terramc.notification.error.api.serverStats").color(
                TextColor.color(255, 85, 85)));
            return;
          }
          JsonObject jsonObject = response.get();

          ServerInfoData.Information.setRegisteredPlayers(jsonObject.get("registeredPlayers").getAsInt());

          if(jsonObject.has("serverStats")) {
            JsonObject serverStats = jsonObject.get("serverStats").getAsJsonObject();
            ServerInfoData.Information.setMaxPlayers(serverStats.get("maxPlayers").getAsInt());
            ServerInfoData.Information.setMaxPlayersToday(serverStats.get("maxPlayersToday").getAsInt());
            ServerInfoData.Information.setVotes(serverStats.get("votes").getAsInt());
          }
          if(jsonObject.has("groupStats")) {
            ServerInfoData.MaxPlayers.data = jsonObject.get("groupStats").getAsString().replace("&", "§");
          }

        });
  }

  public void loadRankData(UUID playerUuid) {
    AddonData.getToggleRankMap().clear();
    AddonData.getStaffRankMap().clear();
    AddonData.getNickedMap().clear();

    Request.ofGson(JsonObject.class)
        .url(baseUrl + "staff?req=staffData&uuid="+playerUuid+"&source=Addon_LM4", new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            this.addon.pushNotification(Component.translatable("terramc.notification.error.title"), Component.translatable("terramc.notification.error.api.data").color(
                TextColor.color(255, 85, 851)));
            return;
          }
          JsonObject jsonObject = response.get();

          AddonData.setRank(jsonObject.get("Global").getAsJsonObject().get("Rank").getAsString());

          addon.terraMainActivity.updateStaffActivity();

          JsonArray rankArray = jsonObject.get("Ranks").getAsJsonArray();
          for(int i = 0; i < rankArray.size(); i++) {
            JsonObject object = rankArray.get(i).getAsJsonObject();
            object.entrySet().forEach(entry -> {
              String uuid = entry.getKey();
              int rankId = entry.getValue().getAsInt();
              if(StaffGroup.byId(rankId) != null) {
                AddonData.getStaffRankMap().put(UUID.fromString(uuid), StaffGroup.byId(rankId));
              }
            });
          }

          JsonArray toggleArray = jsonObject.get("ToggleRank").getAsJsonArray();
          for(int i = 0; i < toggleArray.size(); i++) {
            JsonObject object = toggleArray.get(i).getAsJsonObject();
            object.entrySet().forEach(entry -> {
              String uuid = entry.getKey();
              int status = entry.getValue().getAsInt();
              if(status == 1) {
                AddonData.getToggleRankMap().put(UUID.fromString(uuid), status);
              }
            });
          }

          JsonArray nickArray = jsonObject.get("Nicked").getAsJsonArray();
          for(int i = 0; i < nickArray.size(); i++) {
            JsonObject object = nickArray.get(i).getAsJsonObject();
            object.entrySet().forEach(entry -> {
              String uuid = entry.getKey();
              int status = entry.getValue().getAsInt();
              if(status == 1) {
                AddonData.getNickedMap().put(UUID.fromString(uuid), status);
              }
            });
          }
        });
  }

  public boolean loadPlayerStats(UUID uuid) {
    Request.ofGson(JsonObject.class)
        .url(baseUrl + "stats?uuid="+uuid+"&source=Addon_LM4", new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            PlayerStats.loadedSuccessful = false;
            this.addon.pushNotification(Component.translatable("terramc.notification.error.title"), Component.translatable("terramc.notification.error.api.stats").color(
                TextColor.color(255, 85, 85)));
            switch (response.getStatusCode()) {
              case 400 -> PlayerStats.notLoadedReason = "Bad Request";
              case 401 -> PlayerStats.notLoadedReason = "Source is not trusted";
              case 403 -> PlayerStats.notLoadedReason = "No Addon Version provided";
              case 404 -> PlayerStats.notLoadedReason = "User does not exists";
              case 406 -> PlayerStats.notLoadedReason = "Unable to fetch Stats! Make sure, that you are using the newest version of the addon";
            }
            return;
          }
          JsonObject jsonObject = response.get();
          PlayerStats.loadedSuccessful = true;

          JsonArray bedWarsArray = jsonObject.get("BedWars").getAsJsonArray();
          for(int i = 0; i < bedWarsArray.size(); i++) {
            JsonObject object = bedWarsArray.get(i).getAsJsonObject();
            PlayerStats.BedWars.kills = object.get("Kills").getAsInt();
            PlayerStats.BedWars.deaths = object.get("Deaths").getAsInt();
            PlayerStats.BedWars.kd = PlayerStats.getKD(PlayerStats.BedWars.kills, PlayerStats.BedWars.deaths);
            PlayerStats.BedWars.wins = object.get("Wins").getAsInt();
            PlayerStats.BedWars.looses = object.get("Looses").getAsInt();
            PlayerStats.BedWars.beds = object.get("Beds").getAsInt();
          }

          JsonArray buildFfaArray = jsonObject.get("BuildFFA").getAsJsonArray();
          for(int i = 0; i < buildFfaArray.size(); i++) {
            JsonObject object = buildFfaArray.get(i).getAsJsonObject();
            PlayerStats.BuildFFA.kills = object.get("Kills").getAsInt();
            PlayerStats.BuildFFA.deaths = object.get("Deaths").getAsInt();
            PlayerStats.BuildFFA.kd = PlayerStats.getKD(PlayerStats.BuildFFA.kills, PlayerStats.BuildFFA.deaths);
            PlayerStats.BuildFFA.points = object.get("Points").getAsInt();
          }

          JsonArray kbFfaArray = jsonObject.get("KnockBackFFA").getAsJsonArray();
          for(int i = 0; i < kbFfaArray.size(); i++) {
            JsonObject object = kbFfaArray.get(i).getAsJsonObject();
            PlayerStats.KnockBackFFA.kills = object.get("Kills").getAsInt();
            PlayerStats.KnockBackFFA.deaths = object.get("Deaths").getAsInt();
            PlayerStats.KnockBackFFA.kd = PlayerStats.getKD(PlayerStats.KnockBackFFA.kills, PlayerStats.KnockBackFFA.deaths);
            PlayerStats.KnockBackFFA.points = object.get("Points").getAsInt();
          }

          JsonArray skyWarsArray = jsonObject.get("SkyWars").getAsJsonArray();
          for(int i = 0; i < skyWarsArray.size(); i++) {
            JsonObject object = skyWarsArray.get(i).getAsJsonObject();
            PlayerStats.SkyWars.kills = object.get("Kills").getAsInt();
            PlayerStats.SkyWars.deaths = object.get("Deaths").getAsInt();
            PlayerStats.SkyWars.kd = PlayerStats.getKD(PlayerStats.SkyWars.kills, PlayerStats.SkyWars.deaths);
            PlayerStats.SkyWars.wins = object.get("Wins").getAsInt();
            PlayerStats. SkyWars.looses = object.get("Looses").getAsInt();
          }

          JsonArray labArray = jsonObject.get("TheLab").getAsJsonArray();
          for(int i = 0; i < labArray.size(); i++) {
            JsonObject object = labArray.get(i).getAsJsonObject();
            PlayerStats.TheLab.wins = object.get("Wins").getAsInt();
            PlayerStats.TheLab.looses = object.get("Looses").getAsInt();
          }

          JsonArray ggArray = jsonObject.get("GunGame").getAsJsonArray();
          for(int i = 0; i < ggArray.size(); i++) {
            JsonObject object = ggArray.get(i).getAsJsonObject();
            PlayerStats.GunGame.kills = object.get("Kills").getAsInt();
            PlayerStats.GunGame.deaths = object.get("Deaths").getAsInt();
            PlayerStats.GunGame.kd = PlayerStats.getKD(PlayerStats.GunGame.kills, PlayerStats.GunGame.deaths);
            PlayerStats.GunGame.points = object.get("Points").getAsInt();
            PlayerStats.GunGame.levelRecord = object.get("LevelRecord").getAsInt();
          }

          JsonArray waterFfaArray = jsonObject.get("WaterFFA").getAsJsonArray();
          for(int i = 0; i < waterFfaArray.size(); i++) {
            JsonObject object = waterFfaArray.get(i).getAsJsonObject();
            PlayerStats.WaterFFA.kills = object.get("Kills").getAsInt();
            PlayerStats.WaterFFA.deaths =object.get("Kills").getAsInt();
            PlayerStats.WaterFFA.kd = PlayerStats.getKD(PlayerStats.WaterFFA.kills, PlayerStats.WaterFFA.deaths);
            PlayerStats.WaterFFA.points = object.get("Points").getAsInt();
          }

          JsonArray ffaArray = jsonObject.get("FFA").getAsJsonArray();
          for(int i = 0; i < ffaArray.size(); i++) {
            JsonObject object = ffaArray.get(i).getAsJsonObject();
            PlayerStats.FFA.kills = object.get("Kills").getAsInt();
            PlayerStats.FFA.deaths = object.get("Deaths").getAsInt();
            PlayerStats.FFA.kd = PlayerStats.getKD(PlayerStats.FFA.kills, PlayerStats.FFA.deaths);
            PlayerStats.FFA.points = object.get("Points").getAsInt();
          }

          JsonArray tdmArray = jsonObject.get("TeamDeathMatch").getAsJsonArray();
          for(int i = 0; i < tdmArray.size(); i++) {
            JsonObject object = tdmArray.get(i).getAsJsonObject();
            PlayerStats.TDM.kills = object.get("Kills").getAsInt();
            PlayerStats.TDM.deaths = object.get("Deaths").getAsInt();
            PlayerStats.TDM.kd = PlayerStats.getKD(PlayerStats.TDM.kills, PlayerStats.TDM.deaths);
            PlayerStats.TDM.wins = object.get("Wins").getAsInt();
            PlayerStats.TDM.looses = object.get("Looses").getAsInt();
          }

          JsonArray xpArray = jsonObject.get("XP").getAsJsonArray();
          for(int i = 0; i < xpArray.size(); i++) {
            JsonObject object = xpArray.get(i).getAsJsonObject();
            PlayerStats.XP.kills = object.get("Kills").getAsInt();
            PlayerStats.XP.deaths = object.get("Deaths").getAsInt();
            PlayerStats.XP.kd = PlayerStats.getKD(PlayerStats.XP.kills, PlayerStats.XP.deaths);
            PlayerStats.XP.wins = object.get("Wins").getAsInt();
          }

          JsonArray trainerArray = jsonObject.get("SoupTrainer").getAsJsonArray();
          for(int i = 0; i < trainerArray.size(); i++) {
            JsonObject object = trainerArray.get(i).getAsJsonObject();
            PlayerStats.SoupTrainer.bowls = object.get("Bowls").getAsInt();
            PlayerStats.SoupTrainer.soups = object.get("Soups").getAsInt();
          }
        });
    return PlayerStats.loadedSuccessful;
  }

}
