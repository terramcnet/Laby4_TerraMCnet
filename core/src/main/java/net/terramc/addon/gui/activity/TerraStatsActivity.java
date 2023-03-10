package net.terramc.addon.gui.activity;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.util.ActivityUtil;
import net.terramc.addon.util.ApiUtil;
import net.terramc.addon.util.PlayerStats;

@AutoActivity
@Link("stats.lss")
public class TerraStatsActivity extends Activity {

  private TerraAddon addon;

  public TerraStatsActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ButtonWidget updateButton = ButtonWidget.text(I18n.translate("terramc.ui.activity.stats.updateButton"),
        Icon.texture(ResourceLocation.create("terramc", "textures/ui/update.png")));
    updateButton.addId("stats-update");
    updateButton.setActionListener(() -> {
      long remaining = (PlayerStats.updateCoolDown + PlayerStats.updateTime - System.currentTimeMillis());
      if(remaining > 0) {
        this.addon.pushNotification(I18n.translate("terramc.notification.error.title"), I18n.translate("terramc.notification.error.stats-coolDown"));
      } else {
        if(Laby.references().gameUserService().clientGameUser().getUniqueId() != null) {
          if(this.addon.apiUtil().loadPlayerStats(Laby.references().gameUserService().clientGameUser().getUniqueId())) {
            PlayerStats.updateCoolDown = System.currentTimeMillis();
            this.reload();
            this.addon.pushNotification(I18n.translate("terramc.notification.success.title"), I18n.translate("terramc.notification.success.stats-updated"));
          }
        }
      }
    });

    this.document.addChild(updateButton);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);

    Bounds bounds = this.bounds();
    TextRenderer textRenderer = this.labyAPI.renderPipeline().textRenderer();

    float x = bounds.getWidth() / 4;
    float x2 = x + 150;
    float x3 = x + 300;
    float x4 = x + 450;

    float y = 20;
    float y2 = y +80;
    float y3 = y2 +80;

    float space = 98;

    ActivityUtil.drawCredits(textRenderer, bounds, stack);

    if(ApiUtil.loadedSuccessfulStats) {

      String kills =  I18n.translate("terramc.ui.activity.stats.kills");
      String deaths =  I18n.translate("terramc.ui.activity.stats.deaths");
      String kd =  I18n.translate("terramc.ui.activity.stats.kd");
      String points =  I18n.translate("terramc.ui.activity.stats.points");
      String wins =  I18n.translate("terramc.ui.activity.stats.wins");
      String looses =  I18n.translate("terramc.ui.activity.stats.looses");

      drawString(textRenderer, stack, TerraAddon.doubleLine + "§e§lBuildFFA", x -100, y);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.BuildFFA.kills, x -space, y +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.BuildFFA.deaths, x -space, y +25);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.BuildFFA.kd, x -space, y +35);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.BuildFFA.points, x -space, y +45);

      drawString(textRenderer, stack, TerraAddon.doubleLine + "§6§lKnockBackFFA", x2 -100, y);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §6" + PlayerStats.KnockBackFFA.kills, x2 -space, y +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §6" + PlayerStats.KnockBackFFA.deaths, x2 -space, y +25);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §6" + PlayerStats.KnockBackFFA.kd, x2 -space, y +35);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + points + " §8» §6" + PlayerStats.KnockBackFFA.points, x2 -space, y +45);

      drawString(textRenderer, stack, TerraAddon.doubleLine + "§c§lFFA", x3 -100, y);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §c" + PlayerStats.FFA.kills, x3 -space, y +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §c" + PlayerStats.FFA.deaths, x3 -space, y +25);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §c" + PlayerStats.FFA.kd, x3 -space, y +35);

      // WaterFightFFA
      drawString(textRenderer, stack, TerraAddon.doubleLine + "§9§lWaterFightFFA", x4 -100, y);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §9" + PlayerStats.WaterFFA.kills, x4 -space, y +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §9" + PlayerStats.WaterFFA.deaths, x4 -space, y +25);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §9" + PlayerStats.WaterFFA.kd, x4 -space, y +35);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + points + " §8» §9" + PlayerStats.WaterFFA.points, x4 -space, y +45);

      // XP
      drawString(textRenderer, stack, TerraAddon.doubleLine + "§e§lXP", x -100, y2);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.XP.kills, x -space, y2 +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.XP.deaths, x -space, y2 +25);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.XP.kd, x -space, y2 +35);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.XP.wins, x -space, y2 +45);


      // TeamDeathMatch
      drawString(textRenderer, stack, TerraAddon.doubleLine + "§4§lTeamDeathMatch", x2 -100, y2);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §4" + PlayerStats.TDM.kills, x2 -space, y2 +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §4" + PlayerStats.TDM.deaths, x2 -space, y2 +25);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §4" + PlayerStats.TDM.kd, x2 -space, y2 +35);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + wins + " §8» §4" + PlayerStats.TDM.wins, x2 -space, y2 +45);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + looses + " §8» §4" + PlayerStats.TDM.looses, x2 -space, y2 +55);

      // GunGame
      drawString(textRenderer, stack, TerraAddon.doubleLine + "§6§lGunGame", x3 -100, y2);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §6" + PlayerStats.GunGame.kills, x3 -space, y2 +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §6" + PlayerStats.GunGame.deaths, x3 -space, y2 +25);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §6" + PlayerStats.GunGame.kd, x3 -space, y2 +35);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + points + " §8» §6" + PlayerStats.GunGame.points, x3 -space, y2 +45);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.levelRecord") + " §8» §6" + PlayerStats.GunGame.levelRecord, x3 -space, y2 +55);

      // TheLab
      drawString(textRenderer, stack, TerraAddon.doubleLine + "§b§lTheLab", x4 -100, y2);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + wins + " §8» §b" + PlayerStats.TheLab.wins, x4 -space, y2 +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + looses + " §8» §b" + PlayerStats.TheLab.looses, x4 -space, y2 +25);


      // SkyWars

        /*drawString(Main.doubleLine + "§a§lSkyWars", x -100, y3);

        drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §a" + PlayerStats.SkyWars.kills, x -space, y3 +15);
        drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §a" + PlayerStats.SkyWars.deaths, x -space, y3 +25);
        drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §a" + PlayerStats.SkyWars.kd, x -space, y3 +35);
        drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + wins + " §8» §a" + PlayerStats.SkyWars.wins, x -space, y3 +45);
        drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + looses + " §8» §a" + PlayerStats.SkyWars.looses, x -space, y3 +55);*/

      // BedWars

      drawString(textRenderer, stack, TerraAddon.doubleLine + "§c§lBedWars", x2 -100, y3);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kills + " §8» §c" + PlayerStats.BedWars.kills, x2 -space, y3 +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + deaths + " §8» §c" + PlayerStats.BedWars.deaths, x2 -space, y3 +25);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + kd + " §8» §c" + PlayerStats.BedWars.kd, x2 -space, y3 +35);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + wins + " §8» §c" + PlayerStats.BedWars.wins, x2 -space, y3 +45);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + looses + " §8» §c" + PlayerStats.BedWars.looses, x2 -space, y3 +55);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §" + I18n.translate("terramc.ui.activity.stats.brokenBeds") + " §8» §c" + PlayerStats.BedWars.beds, x2 -space, y3 +65);

      // SoupTrainer
      drawString(textRenderer, stack, TerraAddon.doubleLine + "§b§lSoupTrainer", x4 -100, y3);

      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.bowls") + " §8» §b" + PlayerStats.SoupTrainer.bowls, x4 -space, y3 +15);
      drawString(textRenderer, stack, TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.soups") + " §8» §b" + PlayerStats.SoupTrainer.soups, x4 -space, y3 +25);

    } else {

      textRenderer.text(I18n.translate("terramc.notification.error.api.general"))
          .pos(bounds.getCenterX(), bounds.getCenterY())
          .centered(true)
          .render(stack);

    }

  }

  private void drawString(TextRenderer renderer, Stack stack, String text, float x, float y) {
    renderer.text(text).pos(x, y).render(stack);
  }

}
