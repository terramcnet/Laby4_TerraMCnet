package net.terramc.addon.util;

public class PlayerStats {

  // 5*60*1000 = 300.000
  public static final int updateTime = 300000;
  public static long updateCoolDown = 0;

  public static boolean loadedSuccessful = false;
  public static String notLoadedReason = "";

  public static double getKD(int kills, int deaths) {
    double kd = ((double) kills) / ((double) deaths);
    kd = ((double)((int) (kd*100))) / 100;
    return kd;
  }

  public static class BuildFFA {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int points = 0;
  }

  public static class KnockBackFFA {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int points = 0;
  }

  public static class SoupTrainer {
    public static int soups = 0;
    public static int bowls = 0;
  }

  public static class XP {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int wins = 0;
  }

  public static class TDM {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int wins = 0;
    public static int looses = 0;
  }

  public static class FFA {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int points = 0;
  }

  public static class WaterFFA {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int points = 0;
  }

  public static class GunGame {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int points = 0;
    public static int levelRecord = 0;
  }

  public static class TheLab {
    public static int wins = 0;
    public static int looses = 0;
  }

  public static class SkyWars {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int wins = 0;
    public static int looses = 0;
  }

  public static class BedWars {
    public static int kills = 0;
    public static int deaths = 0;
    public static double kd = 0.0;
    public static int wins = 0;
    public static int looses = 0;
    public static int beds = 0;
  }

}
