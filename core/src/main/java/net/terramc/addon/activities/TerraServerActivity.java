package net.terramc.addon.activities;

import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.ServerData;
import net.terramc.addon.util.Util;

@AutoActivity
@Link("activity.lss")
public class TerraServerActivity extends Activity {

  private TerraAddon addon;

  public TerraServerActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.rankUtil().isAdmin()) {
      ComponentWidget noAccessWidget = ComponentWidget.i18n("terramc.ui.general.noAccess");
      noAccessWidget.addId("no-access");
      this.document.addChild(noAccessWidget);
    }

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);

    Bounds bounds = this.bounds();
    TextRenderer textRenderer = this.labyAPI.renderPipeline().textRenderer();

    Util.drawCredits(this.labyAPI, bounds, stack);

    drawString(textRenderer, stack,"§7Registrierte Spieler §8» §e" + ServerData.Information.getRegisteredPlayers(), 60, 20);
    drawString(textRenderer, stack,"§7Maximale Spieler [Gesamt] §8» §e" + ServerData.Information.getMaxPlayers(), 60, 35);
    drawString(textRenderer, stack,"§7Maximale Spieler [Heute] §8» §e" + ServerData.Information.getMaxPlayersToday(), 60, 50);
    drawString(textRenderer, stack,"§7Votes §8» §e" + ServerData.Information.getVotes(), 60, 65);

    drawString(textRenderer, stack, "§7Aktuelle Server-TPS §8» §e" + ServerData.getServerTps(), 60, 120);
    drawString(textRenderer, stack, "§7Aktuelle CPU-Auslastung §8» §e" + ServerData.getCpuUsage(), 60, 135);
    drawString(textRenderer, stack, "§7Aktuelle RAM-Auslastung §8» §e" + ServerData.getRamUsage(), 60, 150);
    drawString(textRenderer, stack, "§7Automatischer Server Neustart §8» §e" + ServerData.getRestartTime(), 60, 175);

    float dataY = 20;
    float dataX = bounds.getWidth() -300;
    String[] split = ServerData.Information.getMaxPlayerData().split("%nl%");
    for(String data : split) {
      drawString(textRenderer, stack,"§e" + data, dataX, dataY);
      dataY+=10;
      if(dataY == 280) {
        dataX += 150;
        dataY = 60;
      }
    }

  }

  private void drawString(TextRenderer renderer, Stack stack, String text, float x, float y) {
    renderer.text(text).pos(x, y).render(stack);
  }

}
