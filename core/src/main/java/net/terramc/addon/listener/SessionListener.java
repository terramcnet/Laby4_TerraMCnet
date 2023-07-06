package net.terramc.addon.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.gui.TerraNavigationElement;
import net.terramc.addon.util.PlayerStats;
import java.util.UUID;

public class SessionListener {

  private TerraAddon addon;

  public SessionListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent event) {
    UUID newUUID = event.newSession().getUniqueId();
    if(this.addon.apiUtil().loadPlayerStats(newUUID)) {
      PlayerStats.updateCoolDown = System.currentTimeMillis();
    }
    this.addon.apiUtil().loadRankData(newUUID);
    this.addon.apiUtil().loadServerData(newUUID);
    if(this.addon.rankUtil().isStaff()) {
      if(this.addon.labyAPI().navigationService().getById("terramc_main_ui") == null) {
        this.addon.labyAPI().navigationService().register("terramc_main_ui", new TerraNavigationElement(this.addon));
      }
    } else {
      this.addon.labyAPI().navigationService().unregister("terramc_main_ui");
    }
  }

}
