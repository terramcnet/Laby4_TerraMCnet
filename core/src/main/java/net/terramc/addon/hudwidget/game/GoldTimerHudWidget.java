package net.terramc.addon.hudwidget.game;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.game.BedWarsGame;
import net.terramc.addon.data.AddonData;

@SpriteSlot(y = 1, x = 2)
public class GoldTimerHudWidget extends ItemHudWidget<HudWidgetConfig> {

  private final TerraAddon addon;

  public GoldTimerHudWidget(TerraAddon addon) {
    super("goldTimer");
    this.addon = addon;
    //setIcon(Icon.texture(ResourceLocation.create("minecraft", "textures/items/gold_ingot.png")));
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA);
    ItemStack itemStack = Laby.references().itemStackFactory().create(
        ResourceLocation.create("minecraft", "gold_ingot"));
    this.updateItemStack(itemStack);
  }

  @Override
  public void onTick() {
    BedWarsGame bedWarsGame = AddonData.getBedWarsGame();
    if(bedWarsGame != null) {
      int elapsedTime = (int) ((System.currentTimeMillis() - bedWarsGame.getGameStarted()) /1000);
      int timer = 26 - elapsedTime % 26;
      this.updateItemName(Component.text(timer));
    } else {
      this.updateItemName(Component.text(26));
    }
  }

  @Override
  public boolean isVisibleInGame() {
    return this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra() & AddonData.getBedWarsGame() != null;
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.texture(ResourceLocation.create("minecraft", "textures/items/gold_ingot.png"));
  }
}
