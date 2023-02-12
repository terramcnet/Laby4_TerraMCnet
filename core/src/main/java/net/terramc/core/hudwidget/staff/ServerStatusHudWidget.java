package net.terramc.core.hudwidget.staff;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.ServerData;

public class ServerStatusHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraAddon addon;

  private TextLine tpsTextLine;
  private TextLine cpuTextLine;
  private TextLine ramTextLine;

  public ServerStatusHudWidget(TerraAddon addon) {
    super("serverStatus");
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bindCategory(TerraAddon.TERRA_STAFF);
    this.tpsTextLine = createLine("TPS", "20.0");
    this.cpuTextLine = createLine("CPU", "0.0");
    this.ramTextLine = createLine("RAM", "0.0");
    updateTextLine();
  }

  @Override
  public void onTick() {
    updateTextLine();
  }

  private void updateTextLine() {
    this.tpsTextLine.updateAndFlush(ServerData.getServerTps());
    this.tpsTextLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra() & this.addon.rankUtil().isAdmin());

    this.cpuTextLine.updateAndFlush(ServerData.getCpuUsage());
    this.cpuTextLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra() & this.addon.rankUtil().isAdmin());

    this.ramTextLine.updateAndFlush(ServerData.getRamUsage());
    this.ramTextLine.setVisible(this.addon.configuration().enabled().get() & TerraAddon.isConnectedTerra() & this.addon.rankUtil().isAdmin());
  }

}
