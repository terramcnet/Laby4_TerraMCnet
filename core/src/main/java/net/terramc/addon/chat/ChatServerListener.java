package net.terramc.addon.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.labymod.api.event.Subscribe;
import net.terramc.addon.chat.event.ChatServerMessageReceiveEvent;
import net.terramc.addon.data.AddonData;
import java.util.UUID;

public class ChatServerListener {

  @Subscribe
  public void onChatServerMessageReceived(ChatServerMessageReceiveEvent event) {
    JsonObject message = event.message();

    if(message.has("playerStatus") && message.get("playerStatus").isJsonObject()) {
      JsonObject data = message.get("playerStatus").getAsJsonObject();
      UUID uuid = UUID.fromString(data.get("uuid").getAsString());
      if(!AddonData.getUsingAddon().contains(uuid)) {
        AddonData.getUsingAddon().add(uuid);
      }
    }

    if(message.has("retrievedPlayerData")) {
      JsonObject data = message.get("retrievedPlayerData").getAsJsonObject();
      if(data.has("players")) {
        if (data.get("players").isJsonArray()) {
          JsonArray array = data.get("players").getAsJsonArray();
          for (int i = 0; i < array.size(); i++) {
            JsonObject playerData = array.get(i).getAsJsonObject();
            UUID uuid = UUID.fromString(playerData.get("uuid").getAsString());
            if(!AddonData.getUsingAddon().contains(uuid)) {
              AddonData.getUsingAddon().add(uuid);
            }
          }
        }
      }
    }


  }

}
