package net.terramc.addon.hudwidget.staff;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;

@SpriteSlot(x = 4)
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
  public void onTick(boolean isEditorContext) {
    updateTextLine();
  }

  private void updateTextLine() {
    this.vanishTextLine.updateAndFlush(AddonData.isVanish() ? "§aAktiv" : "§cInaktiv");
    this.vanishTextLine.setState(this.addon.configuration().enabled().get() & this.addon.isConnected() & this.addon.rankUtil().isStaff() ? State.VISIBLE : State.HIDDEN);

    this.autoVanishTextLine.updateAndFlush(AddonData.isAutoVanish() ? "§aAktiv" : "§cInaktiv");
    this.autoVanishTextLine.setState(this.addon.configuration().enabled().get() & this.addon.isConnected() & this.addon.rankUtil().isStaff() ? State.VISIBLE : State.HIDDEN);
  }

}
