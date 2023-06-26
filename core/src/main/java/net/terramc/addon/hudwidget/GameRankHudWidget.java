package net.terramc.addon.hudwidget;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;

//@SpriteSlot(x = 2)
public class GameRankHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine textLine;

  public GameRankHudWidget(TerraAddon addon) {
    super("gameRank");
    this.addon = addon;
    setIcon(Icon.texture(ResourceLocation.create("terramc", "textures/hud/game_ranking.png")));
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA);
    this.textLine = createLine("Ranking", "#0");
    updateTextLine();
  }

  @Override
  public void onTick(boolean isEditorContext) {
    updateTextLine();
  }

  private void updateTextLine() {
    this.textLine.updateAndFlush(AddonData.getGameRank());
    this.textLine.setState(this.addon.configuration().enabled().get() & this.addon.isConnected() ? State.VISIBLE : State.HIDDEN);
  }

}
