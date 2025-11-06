package net.terramc.addon.group;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.snapshot.TerraAddonKeys;
import net.terramc.addon.snapshot.TerraChatUserSnapshot;
import net.terramc.addon.util.CustomTextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class TerraGroupTextTag extends ComponentNameTag {

  private TerraAddon addon;
  private @Nullable TerraGroup group;

  public TerraGroupTextTag(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void begin(EntitySnapshot snapshot) {
    this.group = this.getVisibleGroup(snapshot);
    super.begin(snapshot);
  }

  @Override
  protected @NotNull List<Component> buildComponents(EntitySnapshot snapshot) {
    if(this.group == null) return super.buildComponents(snapshot);
    Component groupDisplayName = Component.text().append(Component.text("TERRAMC", TextColor.color(this.addon.configuration().nameTagConfiguration.nameTageColor().get().get())))
        .append(Component.space()).build();
    if(this.addon.configuration().nameTagConfiguration.textDecoration().get() != CustomTextDecoration.NONE) {
      groupDisplayName.decorate(this.addon.configuration().nameTagConfiguration.textDecoration().get().getLabyDecoration());
    }
    groupDisplayName.append(Component.text(this.group.getName(), this.group.getTextColor()));
    return List.of(groupDisplayName);
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
    return this.group != null && super.isVisible();
  }

  private @Nullable TerraGroup getVisibleGroup(EntitySnapshot snapshot) {
    if(!visible(snapshot)) return null;
    if(!snapshot.has(TerraAddonKeys.TERRA_CHAT_USER)) return null;
    TerraChatUserSnapshot chatUserSnapshot = snapshot.get(TerraAddonKeys.TERRA_CHAT_USER);
    if(chatUserSnapshot.getTerraChatUser() == null) return null;
    if(!AddonData.getStaffRankMap().containsKey(chatUserSnapshot.getTerraChatUser().getUuid())) return null;
    if(shouldHide(chatUserSnapshot)) return null;
    return AddonData.getStaffRankMap().get(chatUserSnapshot.getTerraChatUser().getUuid());

  }

  private boolean visible(EntitySnapshot snapshot) {
    return this.addon.configuration().enabled().get() && this.addon.configuration().nameTagConfiguration.enabled().get() &&
        this.addon.configuration().nameTagConfiguration.showTag().get() && !snapshot.isDiscrete() && !snapshot.isInvisible();
  }

  private boolean shouldHide(TerraChatUserSnapshot snapshot) {
    if(snapshot.getTerraChatUser() == null) return true;
    boolean tagHidden = false;
    if(AddonData.getChatUsers().containsKey(snapshot.getTerraChatUser().getUuid())) {
      tagHidden =  AddonData.getChatUsers().get(snapshot.getTerraChatUser().getUuid()).isTagHidden();
    }
    if(this.addon.isConnected()) {
      return AddonData.getToggleRanked().contains(snapshot.getTerraChatUser().getUuid()) ||
          AddonData.getNicked().contains(snapshot.getTerraChatUser().getUuid()) || tagHidden;
    }
    return tagHidden;
  }

}
