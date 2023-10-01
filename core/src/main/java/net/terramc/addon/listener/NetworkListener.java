package net.terramc.addon.listener;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoAddEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.labymod.api.mojang.GameProfile;
import net.labymod.api.util.concurrent.task.Task;
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

    Task.builder(() -> {
      this.addon.broadcastUtil().sendStaffTag();
      this.addon.broadcastUtil().sendUserTag();
    }).delay(3, TimeUnit.SECONDS).build().execute();

    if(!this.addon.configuration().enabled().get()) return;
    if(event.serverData().actualAddress().matches("terramc.net", 25565, true)) {
      this.addon.setConnected(true);
      AddonData.resetValues();
      UUID uuid = Laby.references().gameUserService().clientGameUser().getUniqueId();
      this.addon.apiUtil().loadRankData(uuid);
      this.addon.apiUtil().loadServerData(uuid);

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
  public void onPlayerInfoAdd(PlayerInfoAddEvent event) {
    Task.builder(() -> {
      this.addon.broadcastUtil().sendStaffTag();
      this.addon.broadcastUtil().sendUserTag();
    }).delay(3, TimeUnit.SECONDS).build().execute();
  }

  @Subscribe
  public void onPlayerInfoRemove(PlayerInfoRemoveEvent event) {
    GameProfile profile = event.playerInfo().profile();
    AddonData.getUsingAddon().remove(profile.getUniqueId());
  }

  @Subscribe
  public void onNetworkDisconnect(ServerDisconnectEvent event) {
    AddonData.resetValues();
    AddonData.getShouldHideTag().clear();
    AddonData.getUsingAddon().clear();
    this.addon.setConnected(false);
  }

}
