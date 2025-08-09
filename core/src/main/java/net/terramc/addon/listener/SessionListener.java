package net.terramc.addon.listener;

import java.util.UUID;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.terrachat.protocol.packets.PacketAddonStatistics;
import net.terramc.addon.terrachat.protocol.packets.PacketPlayerStatus;
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
    this.addon.chatClient().sendPacket(new PacketAddonStatistics("remove",
        event.previousSession().getUniqueId(), event.previousSession().getUsername(), "-", "-", false));
    this.addon.chatClient().sendPacket(new PacketAddonStatistics("add",
        newUUID, event.newSession().getUsername(), this.addon.addonInfo().getVersion(), this.addon.labyAPI().minecraft().getVersion(), this.addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));

    this.addon.chatClient().sendPacket(new PacketPlayerStatus(event.previousSession().getUniqueId(), event.previousSession().getUsername(), "OFFLINE",
        this.addon.addonInfo().getVersion(), this.addon.labyAPI().minecraft().getVersion(), false));
    this.addon.chatClient().sendPacket(new PacketPlayerStatus(newUUID, event.newSession().getUsername(), "ONLINE",
        this.addon.addonInfo().getVersion(), this.addon.labyAPI().minecraft().getVersion(), this.addon.configuration().nameTagConfiguration.hideOwnTag().get()));
  }

}
