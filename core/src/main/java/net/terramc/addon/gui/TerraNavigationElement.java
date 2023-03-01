package net.terramc.addon.gui;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.navigation.elements.ScreenNavigationElement;
import net.labymod.api.client.resources.ResourceLocation;
import net.terramc.addon.TerraAddon;

public class TerraNavigationElement extends ScreenNavigationElement {

  public TerraNavigationElement(TerraAddon addon) {
    super(addon.terraMainActivity);
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
}
