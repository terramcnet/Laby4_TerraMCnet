package net.terramc.addon.activities.navigation;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.navigation.elements.ScreenNavigationElement;
import net.labymod.api.client.resources.ResourceLocation;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.group.GroupService;

public class TerraNavigationElement extends ScreenNavigationElement {

  private TerraAddon addon;

  public TerraNavigationElement(TerraAddon addon) {
    super(addon.terraMainActivity);
    this.addon = addon;
  }

  @Override
  public String getWidgetId() {
    return "terramc_main";
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("terramc.ui.navigation.main_ui");
  }

  @Override
  public Icon getIcon() {
    return Icon.texture(ResourceLocation.create("terramc", "textures/icon.png"));
  }

  @Override
  public boolean isVisible() {
    return GroupService.getStaffGroups().contains(AddonData.getRank()) || this.addon.isConnected();
  }
}
