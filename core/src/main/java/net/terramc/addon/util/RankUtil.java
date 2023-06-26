package net.terramc.addon.util;

import net.terramc.addon.data.AddonData;

public class RankUtil {

  public boolean isAdmin() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return rank.equals("Inhaber") || rank.equals("Admin");
  }

  public boolean isStaff() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return rank.equals("Inhaber") || rank.equals("Admin") || rank.equals("SrDev") || rank.equals("Dev") ||
        rank.equals("Content") || rank.equals("SrMod") || rank.equals("Mod") || rank.equals("SrSup") ||
        rank.equals("Sup") || rank.equals("Designer") ||rank.equals("Builder");
  }

  public boolean hasAutoGG() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return isStaff() || rank.equals("Terra") || rank.equals("TerraPlus") || rank.equals("Premium+") ||
        rank.equals("CCreator") || rank.equals("YouTuber") || rank.equals("YouTuber+");
  }

  public boolean hasSpecialAutoGG() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return isStaff() || rank.equals("Terra") || rank.equals("TerraPlus") || rank.equals("Premium+") ||
        rank.equals("CCreator") || rank.equals("YouTuber") || rank.equals("YouTuber+");
  }

  public boolean isPremium() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return rank.equals("Premium") || rank.equals("Hydra") || rank.equals("Terra") || rank.equals("TerraPlus") || rank.equals("Premium+") ||
        rank.equals("YouTuber") || rank.equals("YouTuber+");
  }

  public boolean canControlCloud() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return isAdmin() || rank.equals("SrDev") || rank.equals("Dev") || rank.equals("SrMod");
  }

}
