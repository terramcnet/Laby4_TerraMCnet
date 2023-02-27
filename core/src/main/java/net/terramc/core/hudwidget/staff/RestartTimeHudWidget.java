package net.terramc.core.hudwidget.staff;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.ServerData;

public class RestartTimeHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine textLine;

  public RestartTimeHudWidget(TerraAddon addon) {
    super("restartTime");
    this.addon = addon;
    setIcon(Icon.texture(ResourceLocation.create("terramc", "textures/hud/clock.png")));
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA_STAFF);
    this.textLine = createLine("Restart-Zeit", "0h:0m:00s");
    updateTextLine();
  }

  @Override
  public void onTick() {
    updateTextLine();
  }

  private void updateTextLine() {
    this.textLine.updateAndFlush(ServerData.getRestartTime());
    this.textLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra() & this.addon.rankUtil().isAdmin());
  }

}
