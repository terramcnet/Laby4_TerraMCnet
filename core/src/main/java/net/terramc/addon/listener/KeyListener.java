package net.terramc.addon.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import net.terramc.addon.TerraAddon;

public class KeyListener {

  private final TerraAddon addon;

  public KeyListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onKeyPress(KeyEvent event) {
    if(event.state() != State.PRESS) return;
    if(event.key() != this.addon.configuration().uiHotKey().get()) return;
    if(this.addon.labyAPI().minecraft().minecraftWindow().currentScreen() != null) return;
    this.addon.labyAPI().minecraft().executeNextTick(() -> this.addon.labyAPI().minecraft().minecraftWindow().displayScreen(this.addon.terraMainActivity));
  }

}
