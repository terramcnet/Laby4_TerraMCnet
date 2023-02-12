package net.terramc.core.hudwidget.staff;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.AddonData;

public class VanishHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine vanishTextLine;
  private TextLine autoVanishTextLine;

  public VanishHudWidget(TerraAddon addon) {
    super("vanish");
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA_STAFF);
    this.vanishTextLine = createLine("Vanish", "§4X");
    this.autoVanishTextLine = createLine("Auto. Vanish", "§4X");
    updateTextLine();
  }

  @Override
  public void onTick() {
    updateTextLine();
  }

  private void updateTextLine() {
    this.vanishTextLine.updateAndFlush(AddonData.isVanish() ? "§aAktiv" : "§cInaktiv");
    this.vanishTextLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra() & this.addon.rankUtil().isStaff());
    this.autoVanishTextLine.updateAndFlush(AddonData.isAutoVanish() ? "§aAktiv" : "§cInaktiv");
    this.autoVanishTextLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra() & this.addon.rankUtil().isStaff());
  }

}
