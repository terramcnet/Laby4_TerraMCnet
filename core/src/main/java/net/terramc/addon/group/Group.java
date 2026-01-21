package net.terramc.addon.group;

import net.labymod.api.Laby;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import java.awt.*;

public class Group {

  private int id;
  private String name;
  private String displayName;
  private String colorHex;
  private String minecraftColor;
  private String tagName;
  private String displayTypeName;
  private String iconName;
  private Icon icon;
  private boolean isStaff;

  private transient GroupDisplay displayType;
  private transient Color color = Color.WHITE;
  private transient TextColor textColor = NamedTextColor.WHITE;

  public Group(int id, String name, String displayName, String colorHex, String minecraftColor,
      String tagName, String displayTypeName, String iconName, boolean isStaff) {
    this.id = id;
    this.name = name;
    this.displayName = displayName;
    this.colorHex = colorHex;
    this.minecraftColor = minecraftColor;
    this.tagName = tagName;
    this.displayTypeName = displayTypeName;
    this.iconName = iconName;
    this.isStaff = isStaff;
  }

  public void initialize() {
    if(this.displayTypeName != null) {
      GroupDisplay displayType = GroupDisplay.getDisplay(this.displayTypeName);
      this.displayType = displayType == null ? GroupDisplay.NONE : displayType;
    } else {
      this.displayType = GroupDisplay.NONE;
    }
    if(this.iconName != null) {
      Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
        ResourceLocation iconLocation = ResourceLocation.create("terramc", "textures/" + this.iconName + ".png");
        if(iconLocation.exists()) {
          this.icon = Icon.texture(iconLocation);
        }
      });
    }
    try {
      if(this.colorHex != null && !this.colorHex.isEmpty()) {
        this.color = Color.decode("#" + this.colorHex);
        this.textColor = TextColor.color(this.color.getRGB());
      }
    } catch (Exception ignored) {}
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getColorHex() {
    return colorHex;
  }

  public String getMinecraftColor() {
    return minecraftColor;
  }

  public String getTagName() {
    return tagName;
  }

  public Icon getIcon() {
    return icon;
  }

  public boolean isStaff() {
    return isStaff;
  }

  public GroupDisplay getDisplayType() {
    return displayType;
  }

  public Color getColor() {
    return color;
  }

  public TextColor getTextColor() {
    return textColor;
  }

  @Override
  public String toString() {
    return "Group{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", displayName='" + displayName + '\'' +
        ", colorHex='" + colorHex + '\'' +
        ", minecraftColor='" + minecraftColor + '\'' +
        ", tagName='" + tagName + '\'' +
        ", displayTypeName='" + displayTypeName + '\'' +
        ", iconName='" + iconName + '\'' +
        ", icon=" + icon +
        ", isStaff=" + isStaff +
        ", displayType=" + displayType +
        ", color=" + color +
        ", textColor=" + textColor +
        '}';
  }

  public enum GroupDisplay {
    NONE, BESIDE_NAME, ABOVE_HEAD, BOTH;

    public static GroupDisplay getDisplay(String name) {
      for (GroupDisplay value : values()) {
        if (value.name().equalsIgnoreCase(name)) {
          return value;
        }
      }
      return null;
    }

  }

}
