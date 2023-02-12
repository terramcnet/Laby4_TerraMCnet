package net.terramc.core.data;

import net.terramc.core.util.CloudNotifyType;

public class StaffSettings {

  private static CloudNotifyType cloudNotifyType = CloudNotifyType.CHAT;

  public static CloudNotifyType getCloudNotifyType() {
    return cloudNotifyType;
  }

  public static void setCloudNotifyType(CloudNotifyType cloudNotifyType) {
    StaffSettings.cloudNotifyType = cloudNotifyType;
  }
}
