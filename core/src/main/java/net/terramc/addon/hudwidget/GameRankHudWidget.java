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
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(AddonData.getGameRank() == null ? "#0" : AddonData.getGameRank());
    this.textLine.setState(this.addon.configuration().enabled().get() && this.addon.isConnected() && AddonData.getGameRank() != null ? State.VISIBLE : State.HIDDEN);
  }

}
