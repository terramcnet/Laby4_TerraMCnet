package net.terramc.addon.gui.activity;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.attributes.WidgetAlignment;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.ServerInfoData;
import net.terramc.addon.util.ActivityUtil;

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

    if(!TerraAddon.isConnectedTerra()) {
      ButtonWidget connect = ButtonWidget.text(I18n.translate("terramc.ui.general.connect"),
          Icon.texture(ResourceLocation.create("terramc", "textures/ui/update.png")));
      connect.setActionListener(() -> this.labyAPI.serverController().joinServer("terramc.net"));
      connect.top().set(this.bounds().getCenterY() + 20);
      connect.alignmentX().set(WidgetAlignment.CENTER);

      this.document.addChild(connect);
    }

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);

    Bounds bounds = this.bounds();
    TextRenderer textRenderer = this.labyAPI.renderPipeline().textRenderer();

    ActivityUtil.drawCredits(textRenderer, bounds, stack);

    if(!TerraAddon.isConnectedTerra()) {
      textRenderer.text(I18n.translate("terramc.ui.general.noInfoAvailable"))
          .pos(bounds.getCenterX(), bounds.getCenterY())
          .centered(true)
          .render(stack);

      textRenderer.text(I18n.translate("terramc.ui.general.connectToServer"))
          .pos(bounds.getCenterX(), bounds.getCenterY() + 10)
          .centered(true)
          .render(stack);
      return;
    }

    if(!this.addon.rankUtil().isAdmin()) {
      textRenderer.text(I18n.translate("terramc.ui.general.noAccess"))
          .pos(bounds.getCenterX(), bounds.getCenterY())
          .centered(true)
          .render(stack);
      return;
    }

    drawString(textRenderer, stack,"§7Registrierte Spieler §8» §e" + ServerInfoData.Information.getRegisteredPlayers(), 60, 20);
    drawString(textRenderer, stack,"§7Maximale Spieler [Gesamt] §8» §e" + ServerInfoData.Information.getMaxPlayers(), 60, 35);
    drawString(textRenderer, stack,"§7Maximale Spieler [Heute] §8» §e" + ServerInfoData.Information.getMaxPlayersToday(), 60, 50);
    drawString(textRenderer, stack,"§7Votes §8» §e" + ServerInfoData.Information.getVotes(), 60, 65);

    float dataY = 20;
    float dataX = bounds.getWidth() -300;
    String[] split = ServerInfoData.MaxPlayers.data.split("%nl%");
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
