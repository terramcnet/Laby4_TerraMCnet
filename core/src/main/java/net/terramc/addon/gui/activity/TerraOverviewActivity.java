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
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.ActivityUtil;
import net.terramc.addon.util.Util;

@AutoActivity
@Link("activity.lss")
public class TerraOverviewActivity extends Activity {

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

    float x = bounds.getWidth() / 2 - 70;

    // Rank
    // drawUtils.drawItem(new ItemStack(Item.getItemById(315)), x -100, 35, "");
    drawString(textRenderer, stack, TerraAddon.doubleLine + "§7Rang §8» §e" + AddonData.getRank(), x -75, 39);

    // Global-Coins
    //drawUtils.drawItem(new ItemStack(Item.getItemById(266)), x -100, 55, "");
    drawString(textRenderer, stack, TerraAddon.doubleLine + "§7GlobalCoins §8» §6" + Util.format(AddonData.getCoins()), x - 75, 59);

    // Global-Points

    //drawUtils.drawItem(new ItemStack(Item.getItemById(388)), x -100, 75, "");
    drawString(textRenderer, stack, TerraAddon.doubleLine + "§7GlobalPoints §8» §a" + Util.format(AddonData.getPoints()), x - 75, 79);
    drawString(textRenderer, stack, "§8● §7GlobalPoints Rang §8» §a" + (AddonData.getPointsRank() != null ? AddonData.getPointsRank() : "§cFehler"), x -73, 99);

    // Nick

    //drawUtils.drawItem(new ItemStack(Item.getItemById(421)), x -100, 115, "");
    drawString(textRenderer, stack, TerraAddon.doubleLine + "§7Nickname §8» " + (AddonData.getNickName() != null ? "§d" + AddonData.getNickName() : "§cNicht genickt"), x -75, 119);

    // GameRank
    //drawUtils.drawItem(new ItemStack(Item.getItemById(399)), x -100, 135, "");
    drawString(textRenderer, stack, TerraAddon.doubleLine + "§7GameRank §8» " + (AddonData.getGameRank() == null ? "§cNicht in einer Runde" : "§e" + AddonData.getGameRank()), x -75, 139);

    // OnlineTime
    //drawUtils.drawItem(new ItemStack(Item.getItemById(347)), x -100, 155, "");
    drawString(textRenderer, stack, TerraAddon.doubleLine + "§7Online-Zeit §8» " + (AddonData.getOnlineTime() != null ? AddonData.getOnlineTime() : "§cKeine Zeit erfasst"), x -75, 159);

    // Joins
    //drawUtils.drawItem(new ItemStack(Item.getItemById(381)), x -100, 175, "");
    drawString(textRenderer, stack, TerraAddon.doubleLine + "§7Netzwerk betreten §8» §e" + Util.format(AddonData.getJoins()) + "§7mal", x -75, 179);

  }

  private void drawString(TextRenderer renderer, Stack stack, String text, float x, float y) {
    renderer.text(text).pos(x, y).render(stack);
  }

}
