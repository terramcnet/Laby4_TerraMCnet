package net.terramc.addon.listener;

import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.PlayerStats;

public class NetworkListener {

  private final TerraAddon addon;

  public NetworkListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onNetworkLogin(ServerLoginEvent event) {
    if(!this.addon.configuration().enabled().get()) return;
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
            this.addon.pushNotification(Component.translatable("terramc.notification.success.title"), Component.translatable("terramc.notification.success.stats-updated").color(
                TextColor.color(85, 255, 85)));
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
