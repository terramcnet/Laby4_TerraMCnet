package net.terramc.addon.hudwidget.game;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.game.BedWarsGame;
import net.terramc.addon.data.AddonData;

@SpriteSlot(y = 1, x = 1)
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
    //ItemStack itemStack = Laby.references().itemStackFactory().create(ResourceLocation.create("minecraft", "iron_ingot"));
    //this.updateItemStack(itemStack);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    BedWarsGame bedWarsGame = AddonData.getBedWarsGame();
    if(bedWarsGame != null) {
      int elapsedTime = (int) ((System.currentTimeMillis() - bedWarsGame.getGameStarted()) /1000);
      int timer = 12 - elapsedTime % 12;
      this.updateItemName(Component.text(timer), isEditorContext);
    } else {
      this.updateItemName(Component.text(12), isEditorContext);
    }
  }

  @Override
  public boolean isVisibleInGame() {
    return this.addon.configuration().enabled().get() & this.addon.isConnected() & AddonData.getBedWarsGame() != null;
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.texture(ResourceLocation.create("terramc", "textures/hud/iron_ingot.png"));
  }
}
