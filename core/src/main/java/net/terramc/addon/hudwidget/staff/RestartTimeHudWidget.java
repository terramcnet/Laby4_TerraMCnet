package net.terramc.addon.hudwidget.staff;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.ServerData;

@SpriteSlot(x = 6)
public class RestartTimeHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine textLine;

  public RestartTimeHudWidget(TerraAddon addon) {
    super("restartTime");
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA_STAFF);
    this.textLine = createLine("Restart-Zeit", "0h:0m:00s");
    updateTextLine();
  }

  @Override
  public void onTick(boolean isEditorContext) {
    updateTextLine();
  }

  private void updateTextLine() {
    this.textLine.updateAndFlush(ServerData.getRestartTime());
    this.textLine.setState(this.addon.configuration().enabled().get() & this.addon.isConnected() & this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);
  }

}
