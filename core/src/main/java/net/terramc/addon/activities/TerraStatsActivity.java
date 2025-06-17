package net.terramc.addon.activities;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.activities.widget.StatisticsWidget;
import net.terramc.addon.util.PlayerStats;
import net.terramc.addon.util.Util;

@AutoActivity
@Links({@Link("statistics.lss"), @Link("general.lss")})
public class TerraStatsActivity extends Activity {

  private TerraAddon addon;

  public TerraStatsActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    DivWidget container = new DivWidget().addId("container");

    DivWidget header = new DivWidget().addId("header");
    header.addChild(ComponentWidget.i18n("terramc.ui.activity.stats.title").addId("header-text"));

    ButtonWidget updateButton = ButtonWidget.component(Component.translatable("terramc.ui.activity.stats.updateButton",
            NamedTextColor.GREEN),
        Icon.texture(ResourceLocation.create("terramc", "textures/ui/update.png"))).addId("update-button");
    updateButton.setActionListener(() -> {
      long remaining = (PlayerStats.updateCoolDown + PlayerStats.updateTime - System.currentTimeMillis());
      if(remaining > 0) {
        this.addon.pushNotification(
            Component.translatable("terramc.notification.error.title"),
            Component.translatable("terramc.notification.error.stats-coolDown").color(TextColor.color(255, 85, 85)));
      } else {
        if(Laby.references().gameUserService().clientGameUser().getUniqueId() != null) {
          if(this.addon.apiUtil().loadPlayerStats(Laby.references().gameUserService().clientGameUser().getUniqueId())) {
            PlayerStats.updateCoolDown = System.currentTimeMillis();
            this.reload();
            this.addon.pushNotification(
                Component.translatable("terramc.notification.success.title"),
                Component.translatable("terramc.notification.success.stats-updated").color(TextColor.color(85, 255, 85)));
          }
        }
      }
    });
    header.addChild(updateButton);

    container.addChild(header);

    if(PlayerStats.loadedSuccessful) {

      String kills =  I18n.translate("terramc.ui.activity.stats.kills");
      String deaths =  I18n.translate("terramc.ui.activity.stats.deaths");
      String kd =  I18n.translate("terramc.ui.activity.stats.kd");
      String points =  I18n.translate("terramc.ui.activity.stats.points");
      String played =  I18n.translate("terramc.ui.activity.stats.played");
      String wins =  I18n.translate("terramc.ui.activity.stats.wins");
      String looses =  I18n.translate("terramc.ui.activity.stats.looses");
      String ranking =  I18n.translate("terramc.ui.activity.stats.ranking");

      TilesGridWidget<StatisticsWidget> gridWidget = new TilesGridWidget<>().addId("statistics-grid");

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/bedwars.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.bedWars.kills() + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.bedWars.deaths() + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.bedWars.kd() + "\n" +
              TerraAddon.doubleDots + " §7" + played + " §8» §e" + PlayerStats.bedWars.played() + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.bedWars.wins() + "\n" +
              TerraAddon.doubleDots + " §7" + looses + " §8» §e" + PlayerStats.bedWars.looses() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.bedWars.ranking() + "\n" +
              TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.brokenBeds") + " §8» §e" + PlayerStats.bedWars.additional()[0]));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/buildffa.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.buildFFA.kills() + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.buildFFA.deaths() + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.buildFFA.kd() + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.buildFFA.points() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.buildFFA.ranking()));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/kbffa.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.knockBackFFA.kills() + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.knockBackFFA.deaths() + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.knockBackFFA.kd() + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.knockBackFFA.points() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.knockBackFFA.ranking()));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/ffa.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.ffa.kills() + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.ffa.deaths() + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.ffa.kd() + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.ffa.points() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.ffa.ranking()));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/waterffa.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.waterFFA.kills() + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.waterFFA.deaths() + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.waterFFA.kd() + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.waterFFA.points() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.waterFFA.ranking()));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/xp.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.xp.kills() + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.xp.deaths() + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.xp.kd() + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.xp.wins() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.xp.ranking() + "\n" +
              TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.brokenOres") + " §8» §e" + PlayerStats.xp.additional()[0]));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/tdm.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.teamDeathMatch.kills() + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.teamDeathMatch.deaths() + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.teamDeathMatch.kd() + "\n" +
              TerraAddon.doubleDots + " §7" + played + " §8» §e" + PlayerStats.teamDeathMatch.played() + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.teamDeathMatch.wins() + "\n" +
              TerraAddon.doubleDots + " §7" + looses + " §8» §e" + PlayerStats.teamDeathMatch.looses() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.teamDeathMatch.ranking()));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/gungame.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.gunGame.kills() + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.gunGame.deaths() + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.gunGame.kd() + "\n" +
              TerraAddon.doubleDots + " §7" + points + " §8» §e" + PlayerStats.gunGame.points() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.gunGame.ranking() + "\n" +
              TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.levelRecord") + " §8» §e" + PlayerStats.gunGame.additional()[0]));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/the_lab.png")),
          TerraAddon.doubleDots + " §7" + played + " §8» §e" + PlayerStats.theLab.played() + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.theLab.wins() + "\n" +
              TerraAddon.doubleDots + " §7" + looses + " §8» §e" + PlayerStats.theLab.looses() + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.theLab.ranking()));

      gridWidget.addTile(new StatisticsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/soup_trainer.png")),
          TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.bowls") + " §8» §e" + PlayerStats.soupTrainer.additional()[0] + "\n" +
              TerraAddon.doubleDots + " §7" + I18n.translate("terramc.ui.activity.stats.soups") + " §8» §e" + PlayerStats.soupTrainer.additional()[1] + "\n" +
              TerraAddon.doubleDots + " §7" + ranking + " §8» §e" + PlayerStats.soupTrainer.additional()[2]));

      /*gridWidget.addTile(new StatsWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/stats/skyWars.png")),
          TerraAddon.doubleDots + " §7" + kills + " §8» §e" + PlayerStats.SkyWars.kills + "\n" +
              TerraAddon.doubleDots + " §7" + deaths + " §8» §e" + PlayerStats.SkyWars.deaths + "\n" +
              TerraAddon.doubleDots + " §7" + kd + " §8» §e" + PlayerStats.SkyWars.kd + "\n" +
              TerraAddon.doubleDots + " §7" + wins + " §8» §e" + PlayerStats.SkyWars.wins + "\n" +
              TerraAddon.doubleDots + " §7" + looses + " §8» §c" + PlayerStats.SkyWars.looses);*/

      container.addChild(new ScrollWidget(gridWidget, new ListSession<>()));

    } else {

      container.addChild(ComponentWidget.i18n("terramc.notification.error.api.general").addId("api-error"));
      Component reason = Component.translatable("terramc.notification.error.api.reason").color(TextColor.color(255, 85, 85)).append(
          Component.text(":").color(TextColor.color(85, 85, 85))
      ).append(
          Component.text(" " + PlayerStats.notLoadedReason).color(TextColor.color(170, 0 ,0))
      );
      container.addChild(ComponentWidget.component(reason).addId("api-error-reason"));
    }

    this.document.addChild(container);
    Util.addCredits(this.addon, this.document);
  }

}
