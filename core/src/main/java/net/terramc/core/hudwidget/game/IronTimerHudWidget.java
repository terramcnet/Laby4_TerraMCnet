package net.terramc.core.hudwidget.game;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;
import net.terramc.core.TerraAddon;
import net.terramc.core.game.BedWarsGame;
import net.terramc.core.data.AddonData;

public class IronTimerHudWidget extends ItemHudWidget<HudWidgetConfig> {

  private final TerraAddon addon;

  public IronTimerHudWidget(TerraAddon addon) {
    super("ironTimer");
    this.addon = addon;
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA);
    ItemStack itemStack = Laby.references().itemStackFactory().create(
        ResourceLocation.create("minecraft", "iron_ingot"));
    this.updateItemStack(itemStack);
  }

  @Override
  public void onTick() {
    BedWarsGame bedWarsGame = AddonData.getBedWarsGame();
    if(bedWarsGame != null) {
      int elapsedTime = (int) ((System.currentTimeMillis() - bedWarsGame.getGameStarted()) /1000);
      int timer = 12 - elapsedTime % 12;
      this.updateItemName(Component.text(timer));
    } else {
      this.updateItemName(Component.text(12));
    }
  }

  @Override
  public boolean isVisibleInGame() {
    return this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra() & AddonData.getBedWarsGame() != null;
  }

}
