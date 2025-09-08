package net.terramc.addon.group;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.gfx.pipeline.renderer.text.FontFlags;
import net.labymod.api.client.gfx.pipeline.renderer.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.color.format.ColorFormat;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.CustomTextDecoration;
import org.jetbrains.annotations.Nullable;

public class TerraGroupTextTag extends NameTag {

  private TerraAddon addon;
  private @Nullable TerraGroup group;

  public TerraGroupTextTag(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void begin(Entity entity) {
    this.group = this.visibleGroup(entity);
    super.begin(entity);
  }

  @Override
  public void render(Stack stack, Entity entity) {
    if(this.group == null) return;
    int alpha = (int)((float) Laby.labyAPI().minecraft().options().getBackgroundColorWithOpacity(192) * 255.0F);
    TextRenderer renderer = Laby.references().textRendererProvider().getRenderer();
    Component groupDisplayName = Component.text().append(Component.text("TERRAMC", TextColor.color(this.addon.configuration().nameTagConfiguration.nameTageColor().get().get())))
        .append(Component.space()).build();
    if(this.addon.configuration().nameTagConfiguration.textDecoration().get() != CustomTextDecoration.NONE) {
      groupDisplayName.decorate(this.addon.configuration().nameTagConfiguration.textDecoration().get().getLabyDecoration());
    }
    groupDisplayName.append(Component.text(this.group.getName(), this.group.getTextColor()));
    float width = renderer.getWidth(groupDisplayName);
    renderer.render(stack.getProvider().getPose(), groupDisplayName, -width / 2.0F, 0.0F, -1, 15728880, ColorFormat.ARGB32.pack(0, alpha),
        FontFlags.DISPLAY_MODE_NORMAL);
  }

  @Override
  public float getHeight() {
    return super.getHeight();
  }

  @Override
  public float getScale() {
    return 0.5F;
  }

  @Override
  public boolean isVisible() {
    return !this.entity.isCrouching() && this.visibleGroup(entity) != null;
  }

  private TerraGroup visibleGroup(Entity entity) {
    if(!(entity instanceof Player player)) return null;
    if(player.getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!(this.addon.configuration().nameTagConfiguration.enabled().get() & this.addon.configuration().nameTagConfiguration.showTag().get())) return null;
    if(!AddonData.getStaffRankMap().containsKey(player.getUniqueId())) return null;
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
