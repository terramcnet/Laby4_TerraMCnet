package net.terramc.addon.group;

import net.labymod.api.client.entity.player.badge.renderer.BadgeRenderer;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;

public class StaffTabListRenderer implements BadgeRenderer {

  private TerraAddon addon;

  public StaffTabListRenderer(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void render(Stack stack, float x, float y, NetworkPlayerInfo player) {
      StaffGroup staffGroup = this.visibleGroup(player);
      if(staffGroup != null) {
        //staffGroup.getIcon().render(stack, x - 4.0F, y +1.0F, 10.0F, 6.0F);
        staffGroup.getIcon().render(stack, x, y, 8.0F);
      }
  }

  @Override
  public boolean isVisible(NetworkPlayerInfo player) {
    return this.visibleGroup(player) != null;
  }

  private StaffGroup visibleGroup(NetworkPlayerInfo player) {
    if(player.profile().getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!(this.addon.configuration().nameTagConfiguration.enabled().get() & this.addon.configuration().nameTagConfiguration.showIconInTab().get())) return null;
    if(!AddonData.getStaffRankMap().containsKey(player.profile().getUniqueId())) return null;
    if(shouldHide(player)) return null;
    return AddonData.getStaffRankMap().get(player.profile().getUniqueId());

  }

  private boolean shouldHide(NetworkPlayerInfo player) {
    if(this.addon.isConnected()) {
      if(this.addon.rankUtil().isAdmin()) {
        if(this.addon.configuration().showTagAlways().get()) {
          return false;
        }
      }
      return AddonData.getToggleRankMap().containsKey(player.profile().getUniqueId()) || AddonData.getNickedMap().containsKey(player.profile().getUniqueId());
    }
    return false;
  }

}
