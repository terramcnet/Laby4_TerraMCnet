package net.terramc.addon.activities;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.activities.widget.OverviewWidget;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.Util;

@AutoActivity
@Link("activity.lss")
public class TerraOverviewActivity extends Activity {

  private TerraAddon addon;

  public TerraOverviewActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.isConnected()) {

      ComponentWidget noInfoWidget = ComponentWidget.i18n("terramc.ui.general.noInfoAvailable");
      noInfoWidget.addId("not-connected-info");
      this.document.addChild(noInfoWidget);

      ComponentWidget connectTextWidget = ComponentWidget.i18n("terramc.ui.general.connectToServer");
      connectTextWidget.addId("connect-text");
      this.document.addChild(connectTextWidget);

      ButtonWidget connect = ButtonWidget.text(I18n.translate("terramc.ui.general.connect"),
          Icon.texture(ResourceLocation.create("terramc", "textures/ui/update.png")));
      connect.setActionListener(() -> this.labyAPI.serverController().joinServer("terramc.net"));
      connect.addId("connect-btn");

      this.document.addChild(connect);
      return;
    }

    TilesGridWidget<OverviewWidget> gridWidget = new TilesGridWidget<>();
    gridWidget.addId("gridWidget-overview");

    gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/golden_chestplate.png")),
        TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.rank") + "\n§8» §e" + AddonData.getRank()));

    gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/hud/coin.png")),
        TerraAddon.doubleLine + "§7GlobalCoins\n§8» §6" + Util.format(AddonData.getCoins())));

    gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/emerald.png")),
        TerraAddon.doubleLine + "§7GlobalPoints §8» §a" + Util.format(AddonData.getPoints()) + "\n"
            + "§8● §7GlobalPoints " + I18n.translate("terramc.ui.activity.overview.rank") + " §8» §a" + (AddonData.getPointsRank() != null ? AddonData.getPointsRank() : "§c" + I18n.translate("terramc.ui.activity.overview.error"))));

    gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/ender_eye.png")),
        TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.networkJoined") + "\n§8» §e" + Util.format(AddonData.getJoins())));

    gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/clock.png")),
        TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.playTime") + "\n§8» " + (AddonData.getOnlineTime() != null ? AddonData.getOnlineTime() : "§c" + I18n.translate("terramc.ui.activity.overview.noTime"))));

    gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/hud/name_tag.png")),
        TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.nickName") + "\n§8» " + (AddonData.getNickName() != null ? "§d" + AddonData.getNickName() : "§c" + I18n.translate("terramc.ui.activity.overview.notNicked"))));

    gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/hud/game_ranking.png")),
        TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.gameRank") + "\n§8» " + (AddonData.getGameRank() != null ? "§e" + AddonData.getGameRank() : "§c" + I18n.translate("terramc.ui.activity.overview.notInGame"))));

   this.document.addChild(gridWidget);

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);
    Util.drawCredits(this.labyAPI, this.bounds(), stack);
  }

}
