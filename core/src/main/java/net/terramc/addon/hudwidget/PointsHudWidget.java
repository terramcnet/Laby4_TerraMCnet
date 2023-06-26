package net.terramc.addon.hudwidget;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.Util;

@SpriteSlot(x = 1)
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
  public void onTick(boolean isEditorContext) {
    updateTextLine();
  }

  private void updateTextLine() {
    this.textLine.updateAndFlush(Util.format(AddonData.getPoints()));
    this.textLine.setState(this.addon.configuration().enabled().get() & this.addon.isConnected() ? State.VISIBLE : State.HIDDEN);
  }

}
