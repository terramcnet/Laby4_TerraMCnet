package net.terramc.addon.util;

import java.util.UUID;

public class TerraChatUser {

  private UUID uuid;
  private String userName;
  private String status;
  private String addonVersion;
  private String minecraftVersion;
  private boolean tagHidden;

  public TerraChatUser(UUID uuid, String userName, String status, String addonVersion,
      String minecraftVersion, boolean tagHidden) {
    this.uuid = uuid;
    this.userName = userName;
    this.status = status;
    this.addonVersion = addonVersion;
    this.minecraftVersion = minecraftVersion;
    this.tagHidden = tagHidden;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getUserName() {
    return userName;
  }

  public String getStatus() {
    return status;
  }

  public String getAddonVersion() {
    return addonVersion;
  }

  public String getMinecraftVersion() {
    return minecraftVersion;
  }

  public boolean isTagHidden() {
    return tagHidden;
  }

}
