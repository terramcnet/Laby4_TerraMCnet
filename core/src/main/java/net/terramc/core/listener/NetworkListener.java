package net.terramc.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.labymod.api.util.I18n;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.AddonData;
import net.terramc.core.util.PlayerStats;

public class NetworkListener {

  private final TerraAddon addon;

  public NetworkListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onNetworkLogin(ServerLoginEvent event) {
    if(event.serverData().actualAddress().matches("terramc.net", 25565, true)) {
      this.addon.apiUtil().loadRankData(this.addon.labyAPI().gameUserService().clientGameUser().getUniqueId());
      AddonData.resetValues();
      TerraAddon.setConnectedTerra(true);

      if(this.addon.configuration().updateStatsOnJoin().get()) {
        long remaining = (PlayerStats.updateCoolDown + PlayerStats.updateTime - System.currentTimeMillis());
        if(remaining <= 0) {
          if(this.addon.apiUtil().loadPlayerStats(this.addon.labyAPI().gameUserService().clientGameUser().getUniqueId())) {
            PlayerStats.updateCoolDown = System.currentTimeMillis();
            //this.addon.pushNotification("§2Erfolgreich", "§aDeine Statistiken wurden aktualisiert.");
            this.addon.pushNotification(I18n.translate("terramc.notification.success.title"), I18n.translate("terramc.notification.success.stats-updated"));
          }
        }
      }

    }
  }

  @Subscribe
  public void onNetworkDisconnect(ServerDisconnectEvent event) {
    AddonData.resetValues();
    TerraAddon.setConnectedTerra(false);
  }

}
