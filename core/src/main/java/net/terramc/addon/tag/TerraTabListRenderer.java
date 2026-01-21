package net.terramc.addon.tag;

import net.labymod.api.client.entity.player.badge.renderer.BadgeRenderer;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.group.Group;

public class TerraTabListRenderer extends BadgeRenderer {

  private TerraAddon addon;

  public TerraTabListRenderer(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void render(ScreenContext context, float x, float y, NetworkPlayerInfo player) {
      Group group = this.visibleGroup(player);
      if(group != null) {
        //staffGroup.getIcon().render(context.stack(), x, y, 8.0F);
        context.canvas().submitIcon(group.getIcon(), x +1, y, 8.0F, 8.0F);
      }
  }

  @Override
  public boolean isVisible(NetworkPlayerInfo player) {
    return this.visibleGroup(player) != null;
  }

  private Group visibleGroup(NetworkPlayerInfo player) {
    if(player.profile().getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!(this.addon.configuration().nameTagConfiguration.enabled().get() & this.addon.configuration().nameTagConfiguration.showIconInTab().get())) return null;
    if(shouldHide(player)) return null;
    return AddonData.getStaffRankMap().get(player.profile().getUniqueId());
  }

  private boolean shouldHide(NetworkPlayerInfo playerInfo) {
    if(this.addon.isConnected()) {
      return AddonData.getToggleRanked().contains(playerInfo.profile().getUniqueId()) ||
          AddonData.getNicked().contains(playerInfo.profile().getUniqueId());
    }
    return false;
  }

}
