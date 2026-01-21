package net.terramc.addon.tag;

import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.group.Group;
import net.terramc.addon.snapshot.TerraAddonKeys;
import net.terramc.addon.snapshot.TerraUserSnapshot;
import org.jetbrains.annotations.Nullable;

public class TerraGroupIconTag extends IconTag {

  private TerraAddon addon;
  private @Nullable Group group;

  public TerraGroupIconTag(TerraAddon addon) {
    super(9.0F);
    this.addon = addon;
  }

  @Override
  public void begin(EntitySnapshot snapshot) {
    this.group = this.getVisibleGroup(snapshot);
    super.begin(snapshot);
  }

  @Override
  public boolean isVisible() {
    return this.group != null && super.isVisible();
  }

  @Override
  public Icon getIcon(EntitySnapshot snapshot) {
    return this.group != null ? this.group.getIcon() : super.getIcon(snapshot);
  }

  private @Nullable Group getVisibleGroup(EntitySnapshot snapshot) {
    if(!visible(snapshot)) return null;
    if(!snapshot.has(TerraAddonKeys.TERRA_USER)) return null;
    TerraUserSnapshot userSnapshot = snapshot.get(TerraAddonKeys.TERRA_USER);
    if(shouldHide(userSnapshot)) return null;
    return userSnapshot.getGroup();
  }

  private boolean visible(EntitySnapshot snapshot) {
    return this.addon.configuration().enabled().get() && this.addon.configuration().nameTagConfiguration.enabled().get() &&
        this.addon.configuration().nameTagConfiguration.showIconTag().get() && !snapshot.isDiscrete() && !snapshot.isInvisible();
  }

  private boolean shouldHide(TerraUserSnapshot snapshot) {
    if(snapshot.getUuid() == null) return true;
    if(this.addon.isConnected()) {
      return AddonData.getToggleRanked().contains(snapshot.getUuid()) ||
          AddonData.getNicked().contains(snapshot.getUuid());
    }
    return false;
  }

}
