package net.terramc.addon.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.labymod.api.event.Subscribe;
import net.terramc.addon.chat.event.ChatServerMessageReceiveEvent;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.TerraChatUser;
import java.util.UUID;

public class ChatServerListener {

  @Subscribe
  public void onChatServerMessageReceived(ChatServerMessageReceiveEvent event) {
    JsonObject message = event.message();

    if(message.has("playerStatus") && message.get("playerStatus").isJsonObject()) {
      JsonObject data = message.get("playerStatus").getAsJsonObject();
      UUID uuid = UUID.fromString(data.get("uuid").getAsString());
      String userName = data.get("userName").getAsString();
      String status = data.get("status").getAsString();
      String addonVersion = data.get("addonVersion").getAsString();
      String minecraftVersion = data.has("minecraftVersion") ? data.get("minecraftVersion").getAsString() : "unknown";
      boolean tagHidden = data.get("tagHidden").getAsBoolean();
      AddonData.getChatUsers().put(uuid, new TerraChatUser(uuid, userName, status, addonVersion, minecraftVersion, tagHidden));
    }

    if(message.has("retrievedPlayerData")) {
      JsonObject data = message.get("retrievedPlayerData").getAsJsonObject();
      if(data.has("players")) {
        if (data.get("players").isJsonArray()) {
          JsonArray array = data.get("players").getAsJsonArray();
          for (int i = 0; i < array.size(); i++) {
            JsonObject playerData = array.get(i).getAsJsonObject();
            UUID uuid = UUID.fromString(playerData.get("uuid").getAsString());
            String userName = playerData.get("userName").getAsString();
            String status = playerData.get("status").getAsString();
            String addonVersion = playerData.get("addonVersion").getAsString();
            String minecraftVersion = playerData.has("minecraftVersion") ? playerData.get("minecraftVersion").getAsString() : "unknown";
            boolean tagHidden = playerData.get("tagHidden").getAsBoolean();
            AddonData.getChatUsers().put(uuid, new TerraChatUser(uuid, userName, status, addonVersion, minecraftVersion, tagHidden));
          }
        }
      }
    }


  }

}
