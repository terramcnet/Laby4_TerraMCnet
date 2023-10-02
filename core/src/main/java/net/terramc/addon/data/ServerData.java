package net.terramc.addon.data;

public class ServerData {

  private static String serverTps = "20.0";
  private static String cpuUsage = "0.0";
  private static String ramUsage = "0.0";
  private static String restartTime = "0h:0m:00s";

  public static String getServerTps() {
    return serverTps;
  }

  public static void setServerTps(String serverTps) {
    ServerData.serverTps = serverTps;
  }

  public static String getCpuUsage() {
    return cpuUsage;
  }

  public static void setCpuUsage(String cpuUsage) {
    ServerData.cpuUsage = cpuUsage;
  }

  public static String getRamUsage() {
    return ramUsage;
  }

  public static void setRamUsage(String ramUsage) {
    ServerData.ramUsage = ramUsage;
  }

  public static String getRestartTime() {
    return restartTime;
  }

  public static void setRestartTime(String restartTime) {
    ServerData.restartTime = restartTime;
  }

  public static class Information {

    private static int registeredPlayers = 0;
    private static int maxPlayers = 0;
    private static int maxPlayersToday = 0;
    private static int votes = 0;

    private static String maxPlayerData = "N/A";

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

    public static String getMaxPlayerData() {
      return maxPlayerData;
    }

    public static void setMaxPlayerData(String maxPlayerData) {
      Information.maxPlayerData = maxPlayerData;
    }
  }

}
