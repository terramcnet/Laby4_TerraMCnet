package net.terramc.addon.gui.activity;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.TabbedActivity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.DefaultComponentTab;
import net.terramc.addon.TerraAddon;

@AutoActivity
public class TerraMainActivity extends TabbedActivity {

  private TerraAddon addon;

  public TerraMainActivity(TerraAddon addon) {
    this.addon = addon;
    this.register("terra_overview", new DefaultComponentTab(Component.translatable("terramc.ui.activity.overview.title"), new TerraOverviewActivity(addon)));
    this.register("terra_stats", new DefaultComponentTab(Component.translatable("terramc.ui.activity.stats.title"), new TerraStatsActivity(addon)));
  }

  public void updateStaffActivity() {

    String serverInfoId = "terra_server";
    String staffId = "terra_staff";
    String cloudId = "terra_cloud";

    if(this.addon.rankUtil().isStaff()) {
      if(this.getElementById(staffId) == null) {
        this.register(staffId, new DefaultComponentTab(Component.translatable("terramc.ui.activity.staff"), new TerraStaffActivity(this.addon)));
      }
    } else {
      this.unregister(staffId);
    }

    if(this.addon.rankUtil().isAdmin()) {
      if(this.getElementById(serverInfoId) == null) {
        this.register(serverInfoId, new DefaultComponentTab(Component.translatable("terramc.ui.activity.serverInfo"), new TerraServerActivity(this.addon)));
      }
    } else {
      this.unregister(serverInfoId);
    }

    if(this.addon.rankUtil().canControlCloud()) {
      if(this.getElementById(cloudId) == null) {
        this.register(cloudId, new DefaultComponentTab(Component.translatable("terramc.ui.activity.cloud"), new TerraCloudActivity(this.addon)));
      }
    } else {
      this.unregister(cloudId);
    }

    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

}
