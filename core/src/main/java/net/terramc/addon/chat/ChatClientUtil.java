package net.terramc.addon.chat;

import com.google.gson.JsonObject;
import net.terramc.addon.TerraAddon;

public class ChatClientUtil {

  private TerraAddon addon;
  private ChatClient chatClient;

  public ChatClientUtil(TerraAddon addon, ChatClient chatClient) {
    this.addon = addon;
    this.chatClient = chatClient;
  }

  public void sendRetrievePlayerData(String uuid) {
    JsonObject retrievePlayerDataObject = new JsonObject();
    retrievePlayerDataObject.addProperty("uuid", uuid);
    this.chatClient.sendMessage("retrievePlayerData", retrievePlayerDataObject);
  }

  public void sendPlayerStatus(String uuid, String userName, boolean offline) {
    JsonObject object = new JsonObject();
    object.addProperty("uuid", uuid);
    object.addProperty("userName", userName);
    object.addProperty("status", offline ? "OFFLINE" : "ONLINE");
    object.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    object.addProperty("minecraftVersion", this.addon.labyAPI().minecraft().getVersion());
    object.addProperty("tagHidden", this.addon.configuration().nameTagConfiguration.hideOwnTag().get());
    object.addProperty("development", addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment());
    this.chatClient.sendMessage("playerStatus", object);
  }

  /*public void sendStatistics(boolean quit, String uuid, String userName) {
    JsonObject object = new JsonObject();
    if(!quit) {
      object.addProperty("data", "add");
      object.addProperty("userName", userName);
      object.addProperty("uuid", uuid);
      object.addProperty("addonVersion", addon.addonInfo().getVersion());
      object.addProperty("gameVersion", addon.labyAPI().minecraft().getVersion());
      object.addProperty("development", addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment());
    } else {
      object.addProperty("data", "remove");
      object.addProperty("userName", userName);
      object.addProperty("uuid", uuid);
    }
    this.chatClient.sendMessage("addonStatistics", object);
  }*/

}
