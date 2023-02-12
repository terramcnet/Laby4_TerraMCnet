package net.terramc.core.group;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import net.terramc.core.TerraAddon;
import net.terramc.core.data.AddonData;

public class StaffGroupTextTag extends NameTag {

  private TerraAddon addon;

  public StaffGroupTextTag(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  protected RenderableComponent getRenderableComponent() {
    StaffGroup group = this.visibleGroup(entity);
    if(group != null) {
      return RenderableComponent.of(Component.text("§f§lTERRAMC " + group.getDisplayTitle()));
    }
    return null;
  }

  @Override
  public float getHeight() {
    return super.getHeight();
  }

  @Override
  public float getScale() {
    return 0.6F;
  }

  @Override
  public boolean isVisible() {
    return this.visibleGroup(entity) != null;
  }

  private StaffGroup visibleGroup(Entity entity) {
    if(!(entity instanceof Player)) return null;
    Player player = (Player) entity;
    if(player.getUniqueId() == null) return null;

    if(!(this.addon.configuration().nameTagConfiguration.enabled().get() & this.addon.configuration().nameTagConfiguration.showTag().get())) return null;

    if(!AddonData.getStaffRankMap().containsKey(player.getUniqueId())) return null;

    if(shouldHide(player)) return null;

    return AddonData.getStaffRankMap().get(player.getUniqueId());

  }

  private boolean shouldHide(Player player) {
    if(TerraAddon.isConnectedTerra()) {
      return AddonData.getToggleRankMap().containsKey(player.getUniqueId()) || AddonData.getNickedMap().containsKey(player.getUniqueId());
    }
    return false;
  }

}
