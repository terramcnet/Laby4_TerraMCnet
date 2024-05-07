package net.terramc.addon.hudwidget;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.Util;

@SpriteSlot()
public class CoinsHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine textLine;

  public CoinsHudWidget(TerraAddon addon) {
    super("coins");
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA);
    this.textLine = createLine("GCoins", 0);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Util.format(AddonData.getCoins()));
    this.textLine.setState(this.addon.configuration().enabled().get() && this.addon.isConnected() && AddonData.getCoins() > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
