package net.terramc.core.gui.activity;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.TabbedActivity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.DefaultComponentTab;
import net.terramc.core.TerraAddon;
import net.terramc.core.gui.cloud.CloudBwActivity;
import net.terramc.core.gui.cloud.CloudMainActivity;
import net.terramc.core.gui.cloud.CloudTdmActivity;
import org.jetbrains.annotations.Nullable;

@AutoActivity
public class TerraCloudActivity extends TabbedActivity {

  public TerraCloudActivity(TerraAddon addon) {
    this.register("terra_cloud_main", new DefaultComponentTab(Component.translatable("terramc.ui.activity.cloud.main"), new CloudMainActivity(addon)));
    this.register("terra_cloud_bw", new DefaultComponentTab(Component.translatable("terramc.ui.activity.cloud.bw"), new CloudBwActivity(addon)));
    this.register("terra_cloud_tdm", new DefaultComponentTab(Component.translatable("terramc.ui.activity.cloud.tdm"), new CloudTdmActivity(addon)));
  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return null;
  }
}
