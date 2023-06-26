package net.terramc.addon.group;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.CustomTextDecoration;
import org.jetbrains.annotations.Nullable;

public class StaffGroupTextTag extends NameTag {

  private TerraAddon addon;

  public StaffGroupTextTag(TerraAddon addon) {
    this.addon = addon;
  }

  /*@Override
  protected RenderableComponent getRenderableComponent() {
    StaffGroup group = this.visibleGroup(entity);
    if(group != null) {
      return RenderableComponent.of(Component.text("§f§lTERRAMC " + group.getDisplayTitle()));
    }
    return null;
  }*/

  @Override
  protected @Nullable RenderableComponent getRenderableComponent() {
    StaffGroup group = this.visibleGroup(entity);
    if(group != null) {
      Component component = Component.text("TERRAMC", TextColor.color(this.addon.configuration().nameTagConfiguration.nameTageColor().get().get()));
      if(this.addon.configuration().nameTagConfiguration.textDecoration().get() != CustomTextDecoration.NONE) {
        component.decorate(this.addon.configuration().nameTagConfiguration.textDecoration().get().getLabyDecoration());
      }
      component.append(Component.text(group.getMinecraftColor() + " " + group.getDisplayTitle()));
      return RenderableComponent.of(component);
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

    if(!this.addon.configuration().enabled().get()) return null;

    if(!(this.addon.configuration().nameTagConfiguration.enabled().get() & this.addon.configuration().nameTagConfiguration.showTag().get())) return null;

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
