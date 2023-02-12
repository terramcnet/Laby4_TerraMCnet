package net.terramc.core.hudwidget;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.AddonData;

public class PointsRankHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine textLine;

  public PointsRankHudWidget(TerraAddon addon) {
    super("pointsRank");
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA);
    this.textLine = createLine("Punkte-Rang", "#0");
    updateTextLine();
  }

  @Override
  public void onTick() {
    updateTextLine();
  }

  private void updateTextLine() {
    this.textLine.updateAndFlush(AddonData.getPointsRank());
    this.textLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra());
  }


}
