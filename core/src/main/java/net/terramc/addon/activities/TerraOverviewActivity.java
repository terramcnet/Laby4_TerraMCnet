package net.terramc.addon.activities;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;
import net.labymod.api.util.concurrent.task.Task;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.activities.widget.OverviewWidget;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.terrachat.TerraChatClient.Initiator;
import net.terramc.addon.util.Util;
import java.util.concurrent.TimeUnit;

@AutoActivity
@Links({@Link("overview.lss"), @Link("general.lss")})
public class TerraOverviewActivity extends Activity {

  private TerraAddon addon;

  public TerraOverviewActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    DivWidget container = new DivWidget().addId("container");

    DivWidget header = new DivWidget().addId("header");
    header.addChild(ComponentWidget.i18n("terramc.ui.activity.overview.title").addId("header-text"));
    container.addChild(header);

    if(this.addon.rankUtil().isStaff()) {
      ButtonWidget chatReconnectButton = ButtonWidget.text(I18n.translate("terramc.chat.status.button"));
      chatReconnectButton.setPressable(() -> {
        this.addon.chatClient().disconnect(Initiator.USER, "Reconnect");
        chatReconnectButton.setEnabled(false);
        Task.builder(() -> {
          this.addon.chatClient().connect();
          this.addon.labyAPI().minecraft().executeOnRenderThread(() -> chatReconnectButton.setEnabled(true));
        }).delay(5, TimeUnit.SECONDS).build().execute();
      });
      container.addChild(chatReconnectButton.addId("chat-reconnect-button"));
    }

    Component chatStatus;
    if(this.addon.chatClient().isAuthenticated()) {
      chatStatus = Component.translatable("terramc.chat.status.connected", NamedTextColor.DARK_GREEN);
    } else {
      chatStatus = Component.translatable("terramc.chat.status.disconnected", NamedTextColor.DARK_RED);
    }
    Component chatStatusDisplay = Component.translatable("terramc.chat.title", NamedTextColor.GREEN).append(Component.text(" » ", NamedTextColor.DARK_GRAY));
    container.addChild(ComponentWidget.component(chatStatusDisplay.append(chatStatus)).addId("chat-status"));

    if(this.addon.isConnected()) {

      TilesGridWidget<OverviewWidget> gridWidget = new TilesGridWidget<>().addId("overview-grid");

      gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/golden_chestplate.png")),
          TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.rank") + "\n§8» §e" + AddonData.getRank()));

      gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/hud/coin.png")),
          TerraAddon.doubleLine + "§7GlobalCoins\n§8» §6" + Util.format(AddonData.getCoins())));

      gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/emerald.png")),
          TerraAddon.doubleLine + "§7GlobalPoints §8» §a" + Util.format(AddonData.getPoints()) + "\n"
              + TerraAddon.doubleLine + "§7GlobalPoints " + I18n.translate("terramc.ui.activity.overview.rank") + " §8» §a" + (AddonData.getPointsRank() != null ? AddonData.getPointsRank() : "§c" + I18n.translate("terramc.ui.activity.overview.error"))));

      gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/ender_eye.png")),
          TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.networkJoined") + "\n§8» §e" + Util.format(AddonData.getJoinCount())));

      gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/ui/clock.png")),
          TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.playTime") + "\n§8» " + (AddonData.getPlayTime() != null ? AddonData.getPlayTime() : "§c" + I18n.translate("terramc.ui.activity.overview.noTime"))));

      gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/hud/name_tag.png")),
          TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.nickName") + "\n§8» " + (AddonData.getNickName() != null ? "§d" + AddonData.getNickName() : "§c" + I18n.translate("terramc.ui.activity.overview.notNicked"))));

      gridWidget.addTile(new OverviewWidget(Icon.texture(ResourceLocation.create("terramc", "textures/hud/game_ranking.png")),
          TerraAddon.doubleLine + "§7" + I18n.translate("terramc.ui.activity.overview.gameRank") + "\n§8» " + (AddonData.getGameRank() != null ? "§e" + AddonData.getGameRank() : "§c" + I18n.translate("terramc.ui.activity.overview.notInGame"))));

      container.addChild(gridWidget);

    } else {

      container.addChild(ComponentWidget.i18n("terramc.ui.general.noInfoAvailable").addId("not-connected-info"));
      container.addChild(ComponentWidget.i18n("terramc.ui.general.connectToServer").addId("connect-text"));

      ButtonWidget connectButton = ButtonWidget.text(I18n.translate("terramc.ui.general.connect"),
          Icon.texture(ResourceLocation.create("terramc", "textures/ui/update.png"))).addId("connect-button");
      connectButton.setActionListener(() -> this.labyAPI.serverController().joinServer("terramc.net"));
      container.addChild(connectButton);

    }
    this.document.addChild(container);
    Util.addCredits(this.addon, this.document);
  }

}
