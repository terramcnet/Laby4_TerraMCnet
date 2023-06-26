package net.terramc.addon.group;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import java.awt.*;

public enum StaffGroup {

  INHABER("Inhaber", 0, "§4", Color.decode("#2A0000"), "§4Inhaber", Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/admin.png"))),
  ADMIN("Admin", 1, "§4", Color.decode("#2A0000"), "§4Administrator", Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/admin.png"))),
  DEVELOPMENT("Development", 2, "§c", Color.decode("#3F1515"), "§cDevelopment", Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/development.png"))),
  MODERATION("Moderation", 3, "§6", Color.decode("#2A2A00"), "§6Moderation", Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/moderation.png"))),
  SUPPORT("Support", 4, "§e", Color.decode("#3F3F15"), "§eSupport", Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/support.png"))),
  DESIGN("Design", 5, "§f", Color.decode("#3F3F3F"), "§fDesign", Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/design.png"))),
  BUILDING("Building", 6, "§3", Color.decode("#002A2A"), "§3Building", Icon.texture(ResourceLocation.create("terramc", "textures/staff_icons/build.png"))),
  STAFF("Team", 7, "§a", Color.decode("#153F15"), "§aTeam", Icon.texture(ResourceLocation.create("terramc", "textures/icon.png")));

  private final String name;
  private final int id;
  private final String minecraftColor;
  private final Color color;
  private final String displayTitle;
  private final Icon icon;

  StaffGroup(String name, int id, String minecraftColor, Color color, String displayTitle, Icon icon) {
    this.name = name;
    this.id = id;
    this.minecraftColor = minecraftColor;
    this.color = color;
    this.displayTitle = displayTitle;
    this.icon = icon;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public String getMinecraftColor() {
    return minecraftColor;
  }

  public Color getColor() {
    return color;
  }

  public String getDisplayTitle() {
    return displayTitle;
  }

  public Icon getIcon() {
    return icon;
  }

  public static StaffGroup byId(int id) {
    StaffGroup staffRank = null;
    for(StaffGroup staffRanks : values()) {
      if(staffRanks.getId() == id) {
        staffRank = staffRanks;
      }
    }
    return staffRank;
  }

}
