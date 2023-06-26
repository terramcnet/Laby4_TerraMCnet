package net.terramc.addon.listener;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.labymod.api.util.I18n;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.PlayerStats;
import java.util.UUID;

public class NetworkListener {

  private final TerraAddon addon;

  public NetworkListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onNetworkLogin(ServerLoginEvent event) {
    if(event.serverData().actualAddress().matches("terramc.net", 25565, true)) {
      UUID uuid = Laby.references().gameUserService().clientGameUser().getUniqueId();
      this.addon.apiUtil().loadRankData(uuid);
      this.addon.apiUtil().loadServerData(uuid);
      AddonData.resetValues();
      this.addon.setConnected(true);

      if(this.addon.configuration().updateStatsOnJoin().get()) {
        long remaining = (PlayerStats.updateCoolDown + PlayerStats.updateTime - System.currentTimeMillis());
        if(remaining <= 0) {
          if(this.addon.apiUtil().loadPlayerStats(Laby.references().gameUserService().clientGameUser().getUniqueId())) {
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
    this.addon.setConnected(false);
  }

}
