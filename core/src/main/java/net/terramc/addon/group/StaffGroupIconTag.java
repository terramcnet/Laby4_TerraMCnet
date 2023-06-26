package net.terramc.addon.group;

import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;

public class StaffGroupIconTag extends IconTag {

  private TerraAddon addon;

  public StaffGroupIconTag(TerraAddon addon) {
    super(9.0F);
    this.addon = addon;
  }

  @Override
  public boolean isVisible() {
    return this.visibleGroup(entity) != null;
  }

  @Override
  public int getColor() {
    return super.getColor();
  }

  @Override
  public Icon getIcon() {
    StaffGroup group = this.visibleGroup(entity);
    return group != null ? group.getIcon() : super.getIcon();
  }

  private StaffGroup visibleGroup(Entity entity) {
    if(!(entity instanceof Player)) return null;
    Player player = (Player) entity;
    if(player.getUniqueId() == null) return null;

    if(!this.addon.configuration().enabled().get()) return null;

    if(!(this.addon.configuration().nameTagConfiguration.enabled().get() & this.addon.configuration().nameTagConfiguration.showIconTag().get())) return null;

    if(!AddonData.getStaffRankMap().containsKey(player.getUniqueId())) return null;

    if(shouldHide(player)) return null;

    return AddonData.getStaffRankMap().get(player.getUniqueId());

  }

  private boolean shouldHide(Player player) {
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
