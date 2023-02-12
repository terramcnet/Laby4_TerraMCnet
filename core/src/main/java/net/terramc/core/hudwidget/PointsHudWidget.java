package net.terramc.core.hudwidget;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.AddonData;

public class PointsHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine textLine;

  public PointsHudWidget(TerraAddon addon) {
    super("points");
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA);
    this.textLine = createLine("GPoints", 0);
    updateTextLine();
  }

  @Override
  public void onTick() {
    updateTextLine();
  }

  private void updateTextLine() {
    this.textLine.updateAndFlush(AddonData.getPoints());
    this.textLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra());
  }

}
