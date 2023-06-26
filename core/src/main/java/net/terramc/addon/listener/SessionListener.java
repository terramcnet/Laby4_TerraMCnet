package net.terramc.addon.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.terramc.addon.TerraAddon;
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
      //this.addon.pushNotification("§2Erfolgreich", "§aDeine Statistiken wurden aktualisiert.");
      //this.addon.pushNotification(I18n.translate("terramc.notification.success.title"), I18n.translate("terramc.notification.success.stats-updated"));
    }
    this.addon.apiUtil().loadRankData(newUUID);
    this.addon.apiUtil().loadServerData(newUUID);
  }

}
