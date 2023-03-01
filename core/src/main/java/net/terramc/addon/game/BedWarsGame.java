package net.terramc.addon.game;

public class BedWarsGame {

  private boolean bedAlive;
  private long gameStarted = System.currentTimeMillis();

  public boolean isBedAlive() {
    return bedAlive;
  }

  public void setBedAlive(boolean bedAlive) {
    this.bedAlive = bedAlive;
  }

  public long getGameStarted() {
    return gameStarted;
  }

  public void setGameStarted(long gameStarted) {
    this.gameStarted = gameStarted;
  }
}
