package net.terramc.addon.util;

import net.labymod.api.LabyAPI;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.render.draw.ResourceRenderer;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.addon.TerraAddon;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.UUID;

public class Util {

  public static void drawCredits(LabyAPI labyAPI, Bounds bounds, Stack stack) {
    TextRenderer textRenderer = labyAPI.renderPipeline().textRenderer();
    ResourceRenderer resourceRenderer = labyAPI.renderPipeline().resourceRenderer();

    resourceRenderer.head()
        .player(UUID.fromString("966b5d5e-2577-4ab7-987a-89bfa59da74a"))
        .size(16)
        .pos(5, bounds.getHeight() -20)
        .render(stack);

    textRenderer.text("§7Addon-Version§8: §a" + TerraAddon.instance().addonInfo().getVersion())
        .scale(0.8f)
        .pos(25, bounds.getHeight() -20)
        .render(stack);
    textRenderer.text("§7Developed by §aMisterCore")
        .scale(0.8f)
        .pos(25, bounds.getHeight() -10)
        .render(stack);
  }

  public static String format(int value) {
    DecimalFormat decimalFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN));
    return decimalFormat.format(value);
  }

}
