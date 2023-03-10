package net.terramc.addon.util;

public enum CloudNotifyType {

  CHAT("Chat"),
  NOTIFICATION("Notification"),
  HIDE("Hide");

  private final String name;

  CloudNotifyType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static CloudNotifyType byName(String name) {
    CloudNotifyType cloudNotifyType = CHAT;
    for(CloudNotifyType types : values()) {
      if(types.getName().equalsIgnoreCase(name)) {
        cloudNotifyType = types;
      }
    }
    return cloudNotifyType;
  }

}
