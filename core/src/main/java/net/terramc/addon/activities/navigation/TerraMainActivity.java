package net.terramc.addon.activities.navigation;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.TabbedActivity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.DefaultComponentTab;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.activities.TerraOverviewActivity;
import net.terramc.addon.activities.TerraStatsActivity;

@AutoActivity
public class TerraMainActivity extends TabbedActivity {

  public TerraMainActivity(TerraAddon addon) {
    this.register("terra_overview", new DefaultComponentTab(Component.translatable("terramc.ui.activity.overview.title"), new TerraOverviewActivity(addon)));
    this.register("terra_stats", new DefaultComponentTab(Component.translatable("terramc.ui.activity.stats.title"), new TerraStatsActivity(addon)));
  }

}
