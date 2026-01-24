package net.terramc.addon.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.activity.Document;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.terramc.addon.TerraAddon;

public class Util {

  public static void addCredits(TerraAddon addon, Document document) {

    DivWidget container = new DivWidget().addId("credit-container");

    IconWidget headIcon = new IconWidget(Icon.head(UUID.fromString("966b5d5e-2577-4ab7-987a-89bfa59da74a"))).addId("credits-head");
    container.addChild(headIcon);

    ComponentWidget addonVersionWidget = ComponentWidget.component(Component.text("Addon-Version", NamedTextColor.GRAY)
        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
        .append(Component.text(addon.addonInfo().getVersion(), NamedTextColor.GREEN))
    ).addId("credits-addon-version");
    container.addChild(addonVersionWidget);

    ComponentWidget developerWidget = ComponentWidget.component(Component.text("Developed by ", NamedTextColor.GRAY)
        .append(Component.text("MisterCore", NamedTextColor.GREEN))
    ).addId("credits-developer");
    container.addChild(developerWidget);

    document.addChild(container);
  }

  public static String format(long value) {
    DecimalFormat decimalFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN));
    return decimalFormat.format(value);
  }

}
