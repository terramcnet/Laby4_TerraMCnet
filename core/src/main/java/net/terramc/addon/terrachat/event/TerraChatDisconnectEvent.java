package net.terramc.addon.terrachat.event;


import net.terramc.addon.terrachat.TerraChatClient;
import net.terramc.addon.terrachat.TerraChatClient.Initiator;

public class TerraChatDisconnectEvent extends TerraChatEvent {

  private final String reason;
  private final Initiator initiator;

  public TerraChatDisconnectEvent(TerraChatClient chatClient, Initiator initiator, String reason) {
    super(chatClient);
    this.initiator = initiator;
    this.reason = reason;
  }

  public Initiator getInitiator() {
    return this.initiator;
  }

  public String getReason() {
    return this.reason;
  }

}
