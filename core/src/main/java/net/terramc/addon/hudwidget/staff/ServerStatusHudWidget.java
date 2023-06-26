package net.terramc.addon.hudwidget.staff;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.ServerData;

@SpriteSlot(x = 5)
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
  public void onTick(boolean isEditorContext) {
    updateTextLine();
  }

  private void updateTextLine() {
    this.tpsTextLine.updateAndFlush(ServerData.getServerTps());
    this.tpsTextLine.setState(this.addon.configuration().enabled().get() & this.addon.isConnected() & this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);

    this.cpuTextLine.updateAndFlush(ServerData.getCpuUsage());
    this.cpuTextLine.setState(this.addon.configuration().enabled().get() & this.addon.isConnected() & this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);

    this.ramTextLine.updateAndFlush(ServerData.getRamUsage());
    this.ramTextLine.setState(this.addon.configuration().enabled().get() & this.addon.isConnected() & this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);
  }

}
