package net.terramc.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.terramc.core.TerraAddon;
import net.terramc.core.util.PlayerStats;

public class SessionListener {

  private TerraAddon addon;

  public SessionListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent event) {
      if(this.addon.apiUtil().loadPlayerStats(event.newSession().getUniqueId())) {
        PlayerStats.updateCoolDown = System.currentTimeMillis();
        //this.addon.pushNotification("§2Erfolgreich", "§aDeine Statistiken wurden aktualisiert.");
        //this.addon.pushNotification(I18n.translate("terramc.notification.success.title"), I18n.translate("terramc.notification.success.stats-updated"));
      }
  }

}
