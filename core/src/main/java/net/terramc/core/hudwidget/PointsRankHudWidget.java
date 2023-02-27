package net.terramc.core.hudwidget;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.AddonData;

//@SpriteSlot(x = 3)
public class PointsRankHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine textLine;

  public PointsRankHudWidget(TerraAddon addon) {
    super("pointsRank");
    this.addon = addon;
    setIcon(Icon.texture(ResourceLocation.create("terramc", "textures/hud/point_ranking.png")));
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
