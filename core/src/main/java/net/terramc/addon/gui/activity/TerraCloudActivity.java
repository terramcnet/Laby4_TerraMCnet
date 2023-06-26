package net.terramc.addon.gui.activity;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.gui.activity.widget.CloudControlWidget;
import net.terramc.addon.util.Util;
import java.util.UUID;

@AutoActivity
@Link("cloud.lss")
public class TerraCloudActivity extends Activity {

  private TerraAddon addon;

  public TerraCloudActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.rankUtil().canControlCloud()) return;

    UUID uuid = Laby.references().gameUserService().clientGameUser().getUniqueId();

    TilesGridWidget<CloudControlWidget> gridWidget = new TilesGridWidget<>();
    gridWidget.addId("grid-control");

    // General

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "Proxy", "§aProxy", false));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "DevLobby", "§bDevLobby", false));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "Lobby", "§aLobby", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "PremiumLobby", "§6PremiumLobby", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "SilentLobby", "§4SilentLobby", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TerraUnity", "§aTerraUnity", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TheLab", "§bTheLab", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "WaterFFA", "§9WaterFFA", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "KBFFA", "§6KnockBackFFA", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "ST", "§bSoupTrainer", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "FFA", "§cFFA", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BuildFFA", "§eBuildFFA", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "CB", "§aCityBuild", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "GunGame", "§6GunGame", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "XP", "§eXP", true));

    // BedWars

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-4x1", "§cBedWars-4x1", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-4x2", "§cBedWars-4x2", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-4x4", "§cBedWars-4x4", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-8x1", "§cBedWars-8x1", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-2x2", "§cBedWars-2x2", true));

    // TeamDeathMatch

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TDM-2x1", "§4TeamDeathMatch-2x1", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TDM-2x2", "§4TeamDeathMatch-2x2", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TDM-2x4", "§4TeamDeathMatch-2x4", true));

    ScrollWidget scrollWidget = new ScrollWidget(gridWidget, new ListSession<>());

    this.document.addChild(scrollWidget);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);
    Util.drawCredits(this.labyAPI, this.bounds(), stack);
  }
}
