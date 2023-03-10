package net.terramc.addon.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import net.labymod.api.util.I18n;
import net.labymod.api.util.io.web.request.Request;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.data.StaffSettings;
import net.terramc.addon.group.StaffGroup;

public class ApiUtil {

  private TerraAddon addon;

  public ApiUtil(TerraAddon addon) {
    this.addon = addon;
  }

  private static String baseUrl = "http://api.terramc.net/";

  public static boolean loadedSuccessfulStats = false;

  // actions = [restart, maintenance]
  public void sendControlToProxy(String action, String group) {
    if(!this.addon.rankUtil().canControlCloud()) return;

    try {

      URL url = new URL("http://45.88.108.143:3610/cloud?action="+action+"&group="+group);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      int responseCode = connection.getResponseCode();
      String result = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();

      if(responseCode != 200) {
        this.addon.pushNotification("§4TMC-Proxy Fehler", "§cResponse-Code '" + responseCode + "' von Proxy bekommen.");
        return;
      }

      if(result.startsWith("200")) {
        this.addon.pushNotification("§7§l§o▎§8§l§o▏ §aCloud [TMC-Proxy]", "§aErfolgreich §8[§e" + result + "§8]");
      } else {
        this.addon.pushNotification("§7§l§o▎§8§l§o▏ §4Cloud [TMC-Proxy]", "§cFehler §8[§e" + result + "§8]");
      }

    } catch (Exception ignored) {

    }
  }

  public void sendStaffSetting(UUID uuid, String setting, String value) {
    if(!this.addon.rankUtil().isStaff()) return;

    try {

      URL url = new URL(baseUrl + "staffSettings?uuid="+uuid+"&setting="+setting+"&value="+value);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      int responseCode = connection.getResponseCode();
      String result = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();

      if(responseCode != 200) {
        this.addon.pushNotification("§4TMC-API Fehler", "§cResponse-Code '" + responseCode + "' von API bekommen.");
        return;
      }

      this.addon.pushNotification("§7§l§o▎§8§l§o▏ §aAPI [Settings]", "§e" + result);

    } catch (Exception ignored) {

    }
  }

  public void loadStaffSettings(UUID uuid) {
    Request.ofGson(JsonObject.class)
        .url(baseUrl + "staffSettings?uuid="+uuid+"&load=true", new Object[0])
        .async()
        .execute(response -> {
          if(!response.isPresent()) return;
          JsonObject jsonObject = response.get();

          JsonArray settings = jsonObject.get("Settings").getAsJsonArray();
          for(int i = 0; i < settings.size(); i++) {
            JsonObject object = settings.get(i).getAsJsonObject();
            object.entrySet().forEach(entry -> {
              String setting = entry.getKey();
              String value = entry.getValue().getAsString();
              if(setting.equals("CloudNotify")) {
                StaffSettings.setCloudNotifyType(CloudNotifyType.byName(value));
              }
            });
          }
        });
    /*URLResolver.readJson(baseUrl + "staffSettings?uuid="+uuid+"&load=true", true, new WebResponse<JsonElement>() {
      @Override
      public void success(JsonElement result) {

        JsonObject jsonObject = result.getAsJsonObject();

        JsonArray settings = jsonObject.get("Settings").getAsJsonArray();
        for(int i = 0; i < settings.size(); i++) {
          JsonObject object = settings.get(i).getAsJsonObject();
          object.entrySet().forEach(entry -> {
            String setting = entry.getKey();
            String value = entry.getValue().getAsString();
            if(setting.equals("CloudNotify")) {
              StaffSettings.setCloudNotifyType(CloudNotifyType.byName(value));
            }
          });
        }

      }

      @Override
      public void failed(WebRequestException exception) {
        //addon.pushNotification(I18n.translate("terramc.notification.error.title"), I18n.translate("terramc.notification.error.api.data"));
      }
    });*/

  }

  public void loadRankData(UUID playerUuid) {
    AddonData.getToggleRankMap().clear();
    AddonData.getStaffRankMap().clear();
    AddonData.getNickedMap().clear();

    Request.ofGson(JsonObject.class)
        .url(baseUrl + "staff?uuid="+playerUuid+"&source=Addon_LM4", new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            addon.pushNotification(I18n.translate("terramc.notification.error.title"), I18n.translate("terramc.notification.error.api.data"));
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
              if(StaffGroup.getById(rankId) != null) {
                AddonData.getStaffRankMap().put(UUID.fromString(uuid), StaffGroup.getById(rankId));
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
            loadedSuccessfulStats = false;
            addon.pushNotification(I18n.translate("terramc.notification.error.title"), I18n.translate("terramc.notification.error.api.stats"));
            return;
          }
          JsonObject jsonObject = response.get();
          loadedSuccessfulStats = true;

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
    return loadedSuccessfulStats;
  }

}
