package net.terramc.addon.terrachat.event;

import net.labymod.api.event.Event;
import net.terramc.addon.terrachat.TerraChatClient;

public class TerraChatEvent implements Event {

  private final TerraChatClient chatClient;

  protected TerraChatEvent(TerraChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public TerraChatClient chatClient() {
    return chatClient;
  }

}
