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
}
