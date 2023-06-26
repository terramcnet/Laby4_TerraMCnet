package net.terramc.addon.data;

public class ServerInfoData {

  public static class MaxPlayers {

    public static String data = "N/A";

  }

  public static class Information {

    private static int registeredPlayers = 0;
    private static int maxPlayers = 0;
    private static int maxPlayersToday = 0;
    private static int votes = 0;

    public static void loadData(String raw) {
      String[] split = raw.split(";");
      registeredPlayers = Integer.parseInt(split[0]);
      maxPlayers = Integer.parseInt(split[1]);
      maxPlayersToday = Integer.parseInt(split[2]);
      votes = Integer.parseInt(split[3]);
    }

    public static int getRegisteredPlayers() {
      return registeredPlayers;
    }

    public static int getMaxPlayers() {
      return maxPlayers;
    }

    public static int getMaxPlayersToday() {
      return maxPlayersToday;
    }

    public static int getVotes() {
      return votes;
    }

    public static void setRegisteredPlayers(int registeredPlayers) {
      Information.registeredPlayers = registeredPlayers;
    }

    public static void setMaxPlayers(int maxPlayers) {
      Information.maxPlayers = maxPlayers;
    }

    public static void setMaxPlayersToday(int maxPlayersToday) {
      Information.maxPlayersToday = maxPlayersToday;
    }

    public static void setVotes(int votes) {
      Information.votes = votes;
    }
  }

}
