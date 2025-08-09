package net.terramc.addon.terrachat.event;

import net.terramc.addon.terrachat.TerraChatClient;

public class TerraChatStateUpdateEvent extends TerraChatEvent {

  private final TerraChatClient.ChatState state;

  public TerraChatStateUpdateEvent(TerraChatClient chatClient, TerraChatClient.ChatState state) {
    super(chatClient);
    this.state = state;
  }

  public TerraChatClient.ChatState state() {
    return state;
  }

}
