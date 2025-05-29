package net.terramc.addon.chat.event;

import com.google.gson.JsonObject;
import net.labymod.api.event.Event;

public class ChatServerMessageReceiveEvent implements Event {

  private JsonObject message;

  public ChatServerMessageReceiveEvent(final JsonObject message) {
    this.message = message;
  }

  public JsonObject message() {
    return message;
  }
}
