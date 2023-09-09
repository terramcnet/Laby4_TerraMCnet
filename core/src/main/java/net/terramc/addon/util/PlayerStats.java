package net.terramc.addon.util;

public class PlayerStats {

  // 5*60*1000 = 300.000
  public static final int updateTime = 300000;
  public static long updateCoolDown = 0;

  public static boolean loadedSuccessful = false;
  public static String notLoadedReason = "";

  public static Stats bedWars = new Stats();
  public static Stats buildFFA = new Stats();
  public static Stats knockBackFFA = new Stats();
  public static Stats ffa = new Stats();
  public static Stats waterFFA = new Stats();
  public static Stats xp = new Stats();
  public static Stats soupTrainer = new Stats();
  public static Stats theLab = new Stats();
  public static Stats gunGame = new Stats();
  public static Stats teamDeathMatch = new Stats();
  public static Stats skyWars = new Stats();

  public static class Stats {

    private int kills = 0;
    private int deaths = 0;
    private int points = 0;
    private double kd = 0;
    private int wins = 0;
    private int looses = 0;
    private int played = 0;
    private int[] additional = {0};

    public Stats() {}

    public Stats(int kills, int deaths, int points, int wins, int looses, int played) {
      this.kills = kills;
      this.deaths = deaths;
      this.kd = getKD(kills, deaths);
      this.points = points;
      this.wins = wins;
      this.looses = looses;
      this.played = played;
    }

    public Stats(int kills, int deaths, int points) {
      this.kills = kills;
      this.deaths = deaths;
      this.kd = getKD(kills, deaths);
      this.points = points;
    }

    public int kills() {
      return kills;
    }

    public int deaths() {
      return deaths;
    }

    public int points() {
      return points;
    }

    public double kd() {
      return kd;
    }

    public int wins() {
      return wins;
    }

    public int looses() {
      return looses;
    }

    public int played() {
      return played;
    }

    public int[] additional() {
      return additional;
    }

    public Stats additional(int[] additional) {
      this.additional = additional;
      return this;
    }
  }

  public static double getKD(int kills, int deaths) {
    double kd = ((double) kills) / ((double) deaths);
    kd = ((double)((int) (kd*100))) / 100;
    return kd;
  }

}
