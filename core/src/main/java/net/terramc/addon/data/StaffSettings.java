package net.terramc.addon.data;

import net.terramc.addon.util.CloudNotifyType;

public class StaffSettings {

  private static CloudNotifyType cloudNotifyType = CloudNotifyType.CHAT;

  public static CloudNotifyType getCloudNotifyType() {
    return cloudNotifyType;
  }

  public static void setCloudNotifyType(CloudNotifyType cloudNotifyType) {
    StaffSettings.cloudNotifyType = cloudNotifyType;
  }
}
