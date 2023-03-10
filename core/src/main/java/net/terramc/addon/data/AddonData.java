package net.terramc.addon.data;


import net.terramc.addon.game.BedWarsGame;
import net.terramc.addon.group.StaffGroup;

import java.util.HashMap;
import java.util.UUID;

public class AddonData {

  private static HashMap<UUID, StaffGroup> staffRankMap = new HashMap<>();
  private static HashMap<UUID, Integer> toggleRankMap = new HashMap<>();
  private static HashMap<UUID, Integer> nickedMap = new HashMap<>();


  // VIP - Functions
  private static String nickName = null;

  // Normal - Functions

  private static String gameRank = "#0";
  private static int coins = 0;
  private static int points = 0;
  private static String pointsRank = "#0";

  private static String rank = "Spieler";

  private static String onlineTime = null;
  private static int joins = 0;

  // Staff - Functions

  private static boolean vanish = false;
  private static boolean autoVanish = false;

  private static boolean rankToggled = false;

  // Game - Functions

  private static boolean inRound = false;
  private static boolean ggSent = false;
  private static boolean spectator = false;

  private static BedWarsGame bedWarsGame;

  public static void resetValues() {
    nickName = null;
    inRound = false;
    ggSent = false;
    bedWarsGame = null;
    spectator = false;
    gameRank = "#0";
    pointsRank = "#0";
  }

  public static String getRank() {
    return rank;
  }

  public static void setRank(String rank) {
    AddonData.rank = rank;
  }

  public static String getNickName() {
    return nickName;
  }

  public static void setNickName(String nickName) {
      AddonData.nickName = nickName;
  }

  public static String getGameRank() {
    return gameRank;
  }

  public static void setGameRank(String gameRank) {
    AddonData.gameRank = gameRank;
  }

  public static int getCoins() {
    return coins;
  }

  public static void setCoins(int coins) {
    AddonData.coins = coins;
  }

  public static int getPoints() {
    return points;
  }

  public static void setPoints(int points) {
    AddonData.points = points;
  }

  public static String getPointsRank() {
    return pointsRank;
  }

  public static void setPointsRank(String pointsRank) {
    AddonData.pointsRank = pointsRank;
  }

  public static String getOnlineTime() {
    return onlineTime;
  }

  public static void setOnlineTime(String onlineTime) {
    AddonData.onlineTime = onlineTime;
  }

  public static int getJoins() {
    return joins;
  }

  public static void setJoins(int joins) {
    AddonData.joins = joins;
  }

  public static boolean isInRound() {
    return inRound;
  }

  public static void setInRound(boolean inRound) {
    AddonData.inRound = inRound;
  }

  public static boolean isGgSent() {
    return ggSent;
  }

  public static void setGgSent(boolean ggSent) {
    AddonData.ggSent = ggSent;
  }

  public static boolean isSpectator() {
    return spectator;
  }

  public static void setSpectator(boolean spectator) {
    AddonData.spectator = spectator;
  }

  public static BedWarsGame getBedWarsGame() {
    return bedWarsGame;
  }

  public static void setBedWarsGame(BedWarsGame bedWarsGame) {
    AddonData.bedWarsGame = bedWarsGame;
  }

  public static boolean isVanish() {
    return vanish;
  }

  public static void setVanish(boolean vanish) {
    AddonData.vanish = vanish;
  }

  public static boolean isAutoVanish() {
    return autoVanish;
  }

  public static void setAutoVanish(boolean autoVanish) {
    AddonData.autoVanish = autoVanish;
  }

  public static boolean isRankToggled() {
    return rankToggled;
  }

  public static void setRankToggled(boolean rankToggled) {
    AddonData.rankToggled = rankToggled;
  }

  public static HashMap<UUID, StaffGroup> getStaffRankMap() {
    return staffRankMap;
  }

  public static HashMap<UUID, Integer> getToggleRankMap() {
    return toggleRankMap;
  }

  public static HashMap<UUID, Integer> getNickedMap() {
    return nickedMap;
  }
}
