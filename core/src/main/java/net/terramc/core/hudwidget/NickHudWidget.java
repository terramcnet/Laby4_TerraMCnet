package net.terramc.core.hudwidget;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.AddonData;

public class NickHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine textLine;

  public NickHudWidget(TerraAddon addon) {
    super("nickname");
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA);
    this.textLine = createLine("NickName", "N/A");
    updateTextLine();
  }

  @Override
  public void onTick() {
    updateTextLine();
  }

  private void updateTextLine() {
    if(AddonData.getNickName() != null) {
      this.textLine.updateAndFlush(AddonData.getNickName());
    }
    this.textLine.setVisible(AddonData.getNickName() != null & this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra());
  }

}
