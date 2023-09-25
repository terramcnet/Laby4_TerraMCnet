package net.terramc.addon.activities.widget;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

public class OverviewWidget extends SimpleWidget {

  private Icon icon;

  private String title;

  //private String description;

  public OverviewWidget(Icon icon, String title/*, String description*/) {
    this.icon = icon;
    this.title = title;
    //this.description = description;
  }

  public void initialize(Parent parent) {
    super.initialize(parent);
    IconWidget iconWidget = new IconWidget(this.icon);
    iconWidget.addId("entryIcon");
    addChild(iconWidget);
    //ComponentWidget title = ComponentWidget.text(readableTitle(this.title));
    ComponentWidget title = ComponentWidget.text(this.title);
    title.addId("entryTitle");
    //hoverBoxDelay().set(Integer.valueOf(1000));
    //setHoverComponent(Component.text(this.description));
    addChild(title);
  }

  private int getSpaceAmount(String string) {
    int amount = 0;
    for (int i = 0; i < string.length(); i++) {
      if (string.charAt(i) == ' ')
        amount++;
    }
    return amount;
  }

  @SuppressWarnings("unused")
  private String readableTitle(String title) {
    if (title.length() <= 10)
      return title;
    int spaceAmount = getSpaceAmount(title);
    if (spaceAmount >= 2)
      return addLine(title, getSpacePosition(title, 2));
    return addLine(title, getSpacePosition(title, 1));
  }

  private int getSpacePosition(String title, int pos) {
    int position = 0;
    int amount = 0;
    for (int i = 0; i < title.length(); i++) {
      if (title.charAt(i) == ' ') {
        position = i;
        amount++;
        if (amount == pos)
          break;
      }
    }
    return position;
  }

  private String addLine(String str, int position) {
    return str.substring(0, position) + "\n" + str.substring(position);
  }

}
