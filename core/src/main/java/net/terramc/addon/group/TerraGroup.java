package net.terramc.addon.group;

import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import java.util.Arrays;

public enum TerraGroup {

  INHABER("Inhaber", 0, TextColor.color(170, 0, 0), Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/admin.png"))),
  ADMIN("Administration", 1, TextColor.color(170, 0, 0), Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/admin.png"))),
  DEVELOPMENT("Development", 2, TextColor.color(255, 85, 85), Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/development.png"))),
  MODERATION("Moderation", 3, TextColor.color(255, 170, 0), Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/moderation.png"))),
  SUPPORT("Support", 4, TextColor.color(255, 255, 85), Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/support.png"))),
  DESIGN("Design", 5, TextColor.color(255, 255, 255), Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/design.png"))),
  BUILDING("Building", 6, TextColor.color(0, 170 ,170), Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/build.png"))),
  STAFF("Team", 7, TextColor.color(85, 255, 85), Icon.texture(ResourceLocation.create("terramc", "textures/icon.png"))),

  ADDON_USER("Addon-User", -1, null, Icon.texture(ResourceLocation.create("terramc", "textures/icon.png")));

  private final String name;
  private final int id;
  private final TextColor textColor;
  private final Icon icon;

  TerraGroup(String name, int id, TextColor textColor, Icon icon) {
    this.name = name;
    this.id = id;
    this.textColor = textColor;
    this.icon = icon;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public TextColor getTextColor() {
    return textColor;
  }

  public Icon getIcon() {
    return icon;
  }

  public static TerraGroup byId(int id) {
    return Arrays.stream(values()).filter(staffGroups -> staffGroups.getId() == id).findFirst().orElse(null);
  }

}
