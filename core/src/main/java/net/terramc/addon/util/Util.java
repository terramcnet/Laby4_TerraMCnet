package net.terramc.addon.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.UUID;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.render.draw.ResourceRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.addon.TerraAddon;

public class Util {

  public static void drawCredits(TerraAddon addon, LabyAPI labyAPI, Bounds bounds, Stack stack) {
    TextRenderer textRenderer = labyAPI.renderPipeline().textRenderer();
    ResourceRenderer resourceRenderer = labyAPI.renderPipeline().resourceRenderer();

    resourceRenderer.head()
        .player(UUID.fromString("966b5d5e-2577-4ab7-987a-89bfa59da74a"))
        .size(16)
        .pos(5, bounds.getHeight() -20)
        .render(stack);

    textRenderer.text(RenderableComponent.of(
        Component.text("Addon-Version", NamedTextColor.GRAY)
            .append(Component.text(": ", NamedTextColor.DARK_GRAY))
            .append(Component.text(addon.addonInfo().getVersion(), NamedTextColor.GREEN))
        ))
        .scale(0.8f)
        .pos(25, bounds.getHeight() -20)
        .render(stack);
    textRenderer.text(RenderableComponent.of(
        Component.text("Developed by ", NamedTextColor.GRAY)
            .append(Component.text("MisterCore", NamedTextColor.GREEN))
        ))
        .scale(0.8f)
        .pos(25, bounds.getHeight() -10)
        .render(stack);
  }

  public static String format(int value) {
    DecimalFormat decimalFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN));
    return decimalFormat.format(value);
  }

}
