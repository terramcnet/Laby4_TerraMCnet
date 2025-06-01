package net.terramc.addon.group;

import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;

public class TerraGroupIconTag extends IconTag {

  private TerraAddon addon;

  public TerraGroupIconTag(TerraAddon addon) {
    super(9.0F);
    this.addon = addon;
  }

  @Override
  public boolean isVisible() {
    return !this.entity.isCrouching() &&  this.visibleGroup(entity) != null;
  }

  @Override
  public int getColor() {
    return super.getColor();
  }

  @Override
  public Icon getIcon() {
    TerraGroup group = this.visibleGroup(entity);
    return group != null ? group.getIcon() : super.getIcon();
  }

  private TerraGroup visibleGroup(Entity entity) {
    if(!(entity instanceof Player player)) return null;
    if(player.getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!(this.addon.configuration().nameTagConfiguration.enabled().get() & this.addon.configuration().nameTagConfiguration.showIconTag().get())) return null;
    if(!AddonData.getStaffRankMap().containsKey(player.getUniqueId()) && AddonData.getChatUsers().containsKey(player.getUniqueId())) {
      return TerraGroup.ADDON_USER;
    }
    if(shouldHide(player)) return null;
    return AddonData.getStaffRankMap().get(player.getUniqueId());
  }

  private boolean shouldHide(Player player) {
    boolean tagHidden = false;
    if(AddonData.getChatUsers().containsKey(player.profile().getUniqueId())) {
      tagHidden =  AddonData.getChatUsers().get(player.profile().getUniqueId()).isTagHidden();
    }
    if(this.addon.isConnected()) {
      return AddonData.getToggleRanked().contains(player.profile().getUniqueId()) ||
          AddonData.getNicked().contains(player.profile().getUniqueId()) || tagHidden;
    }
    return tagHidden;
  }

}
