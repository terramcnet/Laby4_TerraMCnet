package net.terramc.core.hudwidget;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.AddonData;
import net.terramc.core.util.Util;

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
    //this.setIcon(Icon.texture(ResourceLocation.create("minecraft", "gold_ingot")));
    updateTextLine();
  }

  @Override
  public void onTick() {
    updateTextLine();
  }

  private void updateTextLine() {
    this.textLine.updateAndFlush(Util.format(AddonData.getCoins()));
    this.textLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra());
  }

}
