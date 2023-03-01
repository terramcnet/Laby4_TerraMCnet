package net.terramc.addon.hudwidget.staff;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
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
    //setIcon(Icon.texture(ResourceLocation.create("terramc", "textures/hud/vanish.png")));
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
