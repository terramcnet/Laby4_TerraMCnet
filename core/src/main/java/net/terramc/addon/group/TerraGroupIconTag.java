package net.terramc.addon.group;

import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.snapshot.TerraAddonKeys;
import net.terramc.addon.snapshot.TerraChatUserSnapshot;
import org.jetbrains.annotations.Nullable;

public class TerraGroupIconTag extends IconTag {

  private TerraAddon addon;
  private @Nullable TerraGroup group;

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
    return group != null ? group.getIcon() : super.getIcon(snapshot);
  }

  private @Nullable TerraGroup getVisibleGroup(EntitySnapshot snapshot) {
    if(!visible(snapshot)) return null;
    if(!snapshot.has(TerraAddonKeys.TERRA_CHAT_USER)) return null;
    TerraChatUserSnapshot chatUserSnapshot = snapshot.get(TerraAddonKeys.TERRA_CHAT_USER);
    if(chatUserSnapshot.getTerraChatUser() == null) return null;
    if(!AddonData.getStaffRankMap().containsKey(chatUserSnapshot.getTerraChatUser().getUuid()) &&
        AddonData.getChatUsers().containsKey(chatUserSnapshot.getTerraChatUser().getUuid())) {
      return TerraGroup.ADDON_USER;
    }
    if(shouldHide(chatUserSnapshot)) return null;
    return AddonData.getStaffRankMap().get(chatUserSnapshot.getTerraChatUser().getUuid());
  }

  private boolean visible(EntitySnapshot snapshot) {
    return this.addon.configuration().enabled().get() && this.addon.configuration().nameTagConfiguration.enabled().get() &&
        this.addon.configuration().nameTagConfiguration.showIconTag().get() && !snapshot.isDiscrete() && !snapshot.isInvisible();
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
