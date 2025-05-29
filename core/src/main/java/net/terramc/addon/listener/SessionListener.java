package net.terramc.addon.listener;

import java.util.UUID;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.util.PlayerStats;

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
    this.addon.apiUtil().postAddonStatistics(event.previousSession().getUniqueId().toString(), event.previousSession().getUsername(), false);
    this.addon.apiUtil().postAddonStatistics(newUUID.toString(),event.newSession().getUsername(), true);
    this.addon.chatClient().util().sendPlayerStatus(event.previousSession().getUniqueId().toString(), event.previousSession().getUsername(), true);
    this.addon.chatClient().util().sendPlayerStatus(newUUID.toString(), event.newSession().getUsername(), false);
  }

}
