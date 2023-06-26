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
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.gui.activity.widget.StatsWidget;
import net.terramc.addon.util.PlayerStats;
import net.terramc.addon.util.Util;

@AutoActivity
@Link("activity.lss")
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
    updateButton.addId("stats-update-btn");
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

    if(PlayerStats.loadedSuccessful) {

      String kills =  I18n.translate("terramc.ui.activity.stats.kills");
      String deaths =  I18n.translate("terramc.ui.activity.stats.deaths");
      String kd =  I18n.translate("terramc.ui.activity.stats.kd");
      String points =  I18n.translate("terramc.ui.activity.stats.points");
      String wins =  I18n.translate("terramc.ui.activity.stats.wins");
      String looses =  I18n.translate("terramc.ui.activity.stats.looses");

      TilesGridWidget<StatsWidget> gridWidget = new TilesGridWidget<>();
      gridWidget.addId("gridWidget-stats");

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/buildffa.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.BuildFFA.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.BuildFFA.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.BuildFFA.kd + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.BuildFFA.points));

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/kbffa.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.KnockBackFFA.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.KnockBackFFA.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.KnockBackFFA.kd + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.KnockBackFFA.points));

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/ffa.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.FFA.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.FFA.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.FFA.kd + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.FFA.points));

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/waterffa.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.WaterFFA.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.WaterFFA.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.WaterFFA.kd + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.WaterFFA.points));

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/gungame.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.GunGame.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.GunGame.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.GunGame.kd + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.GunGame.points + "\n" +
              TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.levelRecord") + " §8» §e" + PlayerStats.GunGame.levelRecord));

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/bedwars.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.BedWars.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.BedWars.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.BedWars.kd + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.BedWars.wins + "\n" +
              TerraAddon.doubleDots + " §7" + looses + " §8» §e" + PlayerStats.BedWars.looses + "\n" +
              TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.brokenBeds") + " §8» §e" + PlayerStats.BedWars.beds));

      /*gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/soon.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.SkyWars.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.SkyWars.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.SkyWars.kd + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.SkyWars.wins + "\n" +
              TerraAddon.doubleDots + " §7" + looses + " §8» §c" + PlayerStats.SkyWars.looses);*/
      /*StatsWidget soonWidget = new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/soon.png")),
          TerraAddon.doubleLine + " §aComing Soon..."));*/

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/tdm.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.TDM.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.TDM.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.TDM.kd + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.TDM.wins + "\n" +
              TerraAddon.doubleDots + " §7" + looses + " §8» §e" + PlayerStats.TDM.looses));

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/xp.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.XP.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.XP.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.XP.kd + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.XP.wins));

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/soup_trainer.png")),
          TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.bowls") + " §8» §e" + PlayerStats.SoupTrainer.bowls + "\n" +
              TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.soups") + " §8» §e" + PlayerStats.SoupTrainer.soups));

      gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/the_lab.png")),
          TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.TheLab.wins + "\n" +
              TerraAddon.doubleDots + " §7" + looses + " §8» §e" + PlayerStats.TheLab.looses));


      this.document.addChild(gridWidget);

    }

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);

    Bounds bounds = this.bounds();
    TextRenderer textRenderer = this.labyAPI.renderPipeline().textRenderer();

    Util.drawCredits(this.labyAPI, bounds, stack);
    if(!PlayerStats.loadedSuccessful) {
      textRenderer.text(I18n.translate("terramc.notification.error.api.general"))
          .pos(bounds.getCenterX(), bounds.getCenterY() -5)
          .centered(true)
          .render(stack);
      textRenderer.text(I18n.translate("terramc.notification.error.api.reason") + PlayerStats.notLoadedReason)
          .pos(bounds.getCenterX(), bounds.getCenterY() +5)
          .centered(true)
          .render(stack);
    }
  }

}
