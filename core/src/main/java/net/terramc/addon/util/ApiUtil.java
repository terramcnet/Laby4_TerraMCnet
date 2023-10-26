package net.terramc.addon.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.I18n;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.data.ServerData;
import net.terramc.addon.group.TerraGroup;
import net.terramc.addon.util.PlayerStats.Stats;

public class ApiUtil {

  private TerraAddon addon;

  public ApiUtil(TerraAddon addon) {
    this.addon = addon;
  }

  private static String BASE_URL = "http://api.terramc.net/";

  /*public void postStaffSetting(UUID uuid, String type, Object value) {
    if(!this.addon.rankUtil().isStaff()) return;

    HashMap<String, String> body = new HashMap<>();
    body.put("req", "staffSetting");
    body.put("uuid", uuid.toString());
    body.put("type", type);
    body.put("value", "" + value);

    Request.ofString()
        .url(BASE_URL + "staff")
        .method(Method.POST)
        .body(body)
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            this.addon.pushNotification(Component.translatable("terramc.notification.error.title"),
                Component.translatable("terramc.notification.cloud.error.unknown", Component.text(response.getStatusCode())).color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          this.addon.pushNotification(Component.text(TerraAddon.doubleLine + " ").append(Component.translatable("terramc.ui.staff.settings.title")),
              Component.text("§eAPI §8- §aEinstellung wurde gespeichert."));
        });
  }*/

  // actions = [restart, maintenance]
  public void sendCloudControl(UUID uuid, String action, String group) {
    if(!this.addon.rankUtil().canControlCloud()) return;

    Request.ofString()
        .url("http://45.88.108.143:3610/cloud?uuid="+uuid+"&action="+action+"&group="+group, new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() == 400) {
            this.addon.pushNotification(Component.translatable("terramc.notification.cloud.error.title"),
                Component.translatable("terramc.notification.cloud.error.parameter").color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          if(response.getStatusCode() == 401) {
            this.addon.pushNotification(Component.translatable("terramc.notification.cloud.error.title"),
                Component.translatable("terramc.notification.cloud.error.notAuthorized").color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          if(response.getStatusCode() == 404) {
            this.addon.pushNotification(Component.translatable("terramc.notification.cloud.error.title"),
                Component.translatable("terramc.notification.cloud.error.groupNotFound").color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          if(response.getStatusCode() != 200) {
            this.addon.pushNotification(Component.translatable("terramc.notification.cloud.error.title"),
                Component.translatable("terramc.notification.cloud.error.unknown", Component.text(response.getStatusCode())).color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          this.addon.pushNotification(Component.translatable("terramc.notification.cloud.success.title"), Component.text("§e" + response.get()));
        });
  }

  public void postAddonStatistics(boolean insert) {
    HashMap<String, String> body = new HashMap<>();
    if(this.addon.labyAPI().getUniqueId() == null) return;
    if(insert) {
      body.put("request", "insertAddonStatistics");
      body.put("uuid", this.addon.labyAPI().getUniqueId().toString());
      body.put("userName", this.addon.labyAPI().getName());
      body.put("addonVersion", this.addon.addonInfo().getVersion());
      body.put("gameVersion", this.addon.labyAPI().minecraft().getVersion());
    } else {
      body.put("request", "deleteAddonStatistics");
      body.put("uuid", this.addon.labyAPI().getUniqueId().toString());
    }

    Request.ofString()
        .url(BASE_URL + "stats")
        .method(Method.POST)
        .body(body)
        .execute(response -> {});
  }

  public void loadServerData(UUID uuid) {
    Request.ofGson(JsonObject.class)
        .url(BASE_URL + "staff?req=serverData&uuid="+uuid, new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            if(response.getStatusCode() == 401) return;
            this.addon.pushNotification(Component.translatable("terramc.notification.error.title"),
                Component.translatable("terramc.notification.error.api.serverStats").color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          JsonObject jsonObject = response.get();

          ServerData.Information.setRegisteredPlayers(jsonObject.get("registeredPlayers").getAsInt());

          if(jsonObject.has("serverStats")) {
            JsonObject serverStats = jsonObject.get("serverStats").getAsJsonObject();
            ServerData.Information.setMaxPlayers(serverStats.get("maxPlayers").getAsInt());
            ServerData.Information.setMaxPlayersToday(serverStats.get("maxPlayersToday").getAsInt());
            ServerData.Information.setVotes(serverStats.get("votes").getAsInt());
          }
          if(jsonObject.has("groupStats")) {
            ServerData.Information.setMaxPlayerData(jsonObject.get("groupStats").getAsString().replace("&", "§"));
          }

        });
  }

  public void loadRankData(UUID playerUuid) {
    AddonData.getToggleRankMap().clear();
    AddonData.getStaffRankMap().clear();
    AddonData.getNickedMap().clear();

    Request.ofGson(JsonObject.class)
        .url(BASE_URL + "staff?req=staffData&uuid="+playerUuid+"&source=Addon_LM4", new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            this.addon.pushNotification(Component.translatable("terramc.notification.error.title"),
                Component.translatable("terramc.notification.error.api.data").color(
                    TextColor.color(255, 85, 851)));
            return;
          }
          JsonObject jsonObject = response.get();

          JsonObject global = jsonObject.get("Global").getAsJsonObject();

          AddonData.setRank(global.get("Rank").getAsString());

          Laby.labyAPI().minecraft().executeOnRenderThread(() -> addon.terraMainActivity.updateStaffActivity());

          JsonArray rankArray = jsonObject.get("Ranks").getAsJsonArray();
          for(int i = 0; i < rankArray.size(); i++) {
            JsonObject object = rankArray.get(i).getAsJsonObject();
            object.entrySet().forEach(entry -> {
              String uuid = entry.getKey();
              int rankId = entry.getValue().getAsInt();
              if(TerraGroup.byId(rankId) != null) {
                AddonData.getStaffRankMap().put(UUID.fromString(uuid), TerraGroup.byId(rankId));
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
        .url(BASE_URL + "stats?uuid="+uuid+"&source=Addon_LM4", new Object[0])
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            PlayerStats.loadedSuccessful = false;
            this.addon.pushNotification(Component.translatable("terramc.notification.error.title"), Component.translatable("terramc.notification.error.api.stats").color(
                TextColor.color(255, 85, 85)));
            switch (response.getStatusCode()) {
              case 400 -> PlayerStats.notLoadedReason = I18n.translate("terramc.ui.activity.stats.apiResponse.400");
              case 401 -> PlayerStats.notLoadedReason = I18n.translate("terramc.ui.activity.stats.apiResponse.401");
              case 403 -> PlayerStats.notLoadedReason = I18n.translate("terramc.ui.activity.stats.apiResponse.403");
              case 404 -> PlayerStats.notLoadedReason = I18n.translate("terramc.ui.activity.stats.apiResponse.404");
              case 406 -> PlayerStats.notLoadedReason = I18n.translate("terramc.ui.activity.stats.apiResponse.406");
            }
            return;
          }
          JsonObject jsonObject = response.get();
          PlayerStats.loadedSuccessful = true;

          JsonArray bedWarsArray = jsonObject.get("BedWars").getAsJsonArray();
          for(int i = 0; i < bedWarsArray.size(); i++) {
            JsonObject object = bedWarsArray.get(i).getAsJsonObject();
            PlayerStats.bedWars = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                0,
                object.get("Wins").getAsInt(),
                object.get("Looses").getAsInt(),
                object.get("Played").getAsInt()
            ).additional(new int[] { object.get("Beds").getAsInt() });
          }

          JsonArray buildFfaArray = jsonObject.get("BuildFFA").getAsJsonArray();
          for(int i = 0; i < buildFfaArray.size(); i++) {
            JsonObject object = buildFfaArray.get(i).getAsJsonObject();
            PlayerStats.buildFFA = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                object.get("Points").getAsInt()
            );
          }

          JsonArray kbFfaArray = jsonObject.get("KnockBackFFA").getAsJsonArray();
          for(int i = 0; i < kbFfaArray.size(); i++) {
            JsonObject object = kbFfaArray.get(i).getAsJsonObject();
            PlayerStats.knockBackFFA = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                object.get("Points").getAsInt()
            );
          }

          JsonArray skyWarsArray = jsonObject.get("SkyWars").getAsJsonArray();
          for(int i = 0; i < skyWarsArray.size(); i++) {
            JsonObject object = skyWarsArray.get(i).getAsJsonObject();
            PlayerStats.skyWars = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                0,
                object.get("Wins").getAsInt(),
                object.get("Looses").getAsInt(),
                0
            );
          }

          JsonArray labArray = jsonObject.get("TheLab").getAsJsonArray();
          for(int i = 0; i < labArray.size(); i++) {
            JsonObject object = labArray.get(i).getAsJsonObject();
            PlayerStats.theLab = new Stats(
                  0,
                0,
                0,
                object.get("Wins").getAsInt(),
                object.get("Looses").getAsInt(),
                0
            );
          }

          JsonArray ggArray = jsonObject.get("GunGame").getAsJsonArray();
          for(int i = 0; i < ggArray.size(); i++) {
            JsonObject object = ggArray.get(i).getAsJsonObject();
            PlayerStats.gunGame = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                object.get("Points").getAsInt(),
                0,
                0,
                0
            ).additional(new int[] { object.get("LevelRecord").getAsInt() });
          }

          JsonArray waterFfaArray = jsonObject.get("WaterFFA").getAsJsonArray();
          for(int i = 0; i < waterFfaArray.size(); i++) {
            JsonObject object = waterFfaArray.get(i).getAsJsonObject();
            PlayerStats.waterFFA = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                object.get("Points").getAsInt(),
                0,
                0,
                0
            );
          }

          JsonArray ffaArray = jsonObject.get("FFA").getAsJsonArray();
          for(int i = 0; i < ffaArray.size(); i++) {
            JsonObject object = ffaArray.get(i).getAsJsonObject();
            PlayerStats.ffa = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                object.get("Points").getAsInt(),
                0,
                0,
                0
            );
          }

          JsonArray tdmArray = jsonObject.get("TeamDeathMatch").getAsJsonArray();
          for(int i = 0; i < tdmArray.size(); i++) {
            JsonObject object = tdmArray.get(i).getAsJsonObject();
            PlayerStats.teamDeathMatch = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                0,
                object.get("Wins").getAsInt(),
                object.get("Looses").getAsInt(),
                object.get("Played").getAsInt()
            );
          }

          JsonArray xpArray = jsonObject.get("XP").getAsJsonArray();
          for(int i = 0; i < xpArray.size(); i++) {
            JsonObject object = xpArray.get(i).getAsJsonObject();
            PlayerStats.xp = new Stats(
                object.get("Kills").getAsInt(),
                object.get("Deaths").getAsInt(),
                0,
                object.get("Wins").getAsInt(),
                0,
                0
            ).additional(new int[]{object.get("BrokenOres").getAsInt()});
          }

          JsonArray trainerArray = jsonObject.get("SoupTrainer").getAsJsonArray();
          for(int i = 0; i < trainerArray.size(); i++) {
            JsonObject object = trainerArray.get(i).getAsJsonObject();
            PlayerStats.soupTrainer = new Stats().additional(new int[] {
                object.get("Bowls").getAsInt(),
                object.get("Soups").getAsInt()
            });
          }
        });
    return PlayerStats.loadedSuccessful;
  }

}
