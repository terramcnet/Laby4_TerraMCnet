package net.terramc.addon.group;

import com.google.gson.JsonObject;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.logging.Logging;
import net.terramc.addon.util.ApiUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupService {

  private final Logging logging = Logging.create("TMC-GroupService");

  private static Map<String, Group> groups = new HashMap<>();

  private static List<String> staffGroups = new ArrayList<>();
  private static List<String> specialPremiumGroups = new ArrayList<>();
  private static List<String> premiumGroups = new ArrayList<>();

  public void loadGroups() {
    groups.clear();
    staffGroups.clear();
    specialPremiumGroups.clear();
    premiumGroups.clear();
    Request.ofGson(JsonObject.class)
        .url(ApiUtil.BASE_URL + "/groups")
        .async()
        .connectTimeout(5000)
        .readTimeout(5000)
        .addHeader("User-Agent", "TerraMC LabyMod 4 Addon")
        .execute(response -> {
          if(response.hasException()) {
            this.logging.error("Failed to load groups", response.exception());
            return;
          }
          JsonObject object = response.get();
          if(object.has("groups") && object.get("groups").isJsonArray()) {
            object.get("groups").getAsJsonArray().forEach(jsonElement -> {
              if(jsonElement.isJsonObject()) {
                JsonObject groupObject = jsonElement.getAsJsonObject();
                Group group = new Group(
                    groupObject.get("id").getAsInt(),
                    groupObject.get("name").getAsString(),
                    !groupObject.get("displayName").getAsString().isEmpty() ? groupObject.get("displayName").getAsString() : null,
                    groupObject.get("color_hex").getAsString(),
                    groupObject.get("color_minecraft").getAsString(),
                    !groupObject.get("tag_name").getAsString().isEmpty() ? groupObject.get("tag_name").getAsString() : null,
                    groupObject.get("display_type").getAsString(),
                    !groupObject.get("icon_name").getAsString().isEmpty() ? groupObject.get("icon_name").getAsString() : null,
                    groupObject.get("is_staff").getAsBoolean()
                );
                group.initialize();
                groups.put(group.getName(), group);
              }
            });
          }

          if(object.has("staff_groups") && object.get("staff_groups").isJsonArray()) {
            object.get("staff_groups").getAsJsonArray().forEach(jsonElement -> {
              if(jsonElement.isJsonPrimitive()) {
                staffGroups.add(jsonElement.getAsString());
              }
            });
          }

          if(object.has("special_premium_groups") && object.get("special_premium_groups").isJsonArray()) {
            object.get("special_premium_groups").getAsJsonArray().forEach(jsonElement -> {
              if(jsonElement.isJsonPrimitive()) {
                specialPremiumGroups.add(jsonElement.getAsString());
              }
            });
          }

          if(object.has("premium_groups") && object.get("premium_groups").isJsonArray()) {
            object.get("premium_groups").getAsJsonArray().forEach(jsonElement -> {
              if(jsonElement.isJsonPrimitive()) {
                premiumGroups.add(jsonElement.getAsString());
              }
            });
          }

        });
  }

  public static Group getGroup(String name) {
    return groups.getOrDefault(name, null);
  }

  public static Group getGroup(int id) {
    Group group = null;
    for(Group value : groups.values()) {
      if(value.getId() == id) {
        group = value;
        break;
      }
    }
    return group;
  }

  public Collection<Group> getGroups() {
    return groups.values();
  }

  public static List<String> getStaffGroups() {
    return staffGroups;
  }

  public static List<String> getSpecialPremiumGroups() {
    return specialPremiumGroups;
  }

  public static List<String> getPremiumGroups() {
    return premiumGroups;
  }

}
