package net.terramc.addon.util;

import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.addon.TerraAddon;

public class ActivityUtil {

  public static void drawCredits(TextRenderer textRenderer, Bounds bounds, Stack stack) {
    textRenderer.text("§7Addon-Version§8: §a" + TerraAddon.getInstance().addonInfo().getVersion())
            .scale(0.8f)
            .pos(5, bounds.getHeight() -20)
                .render(stack);
    textRenderer.text("§7Developed by §aMisterCore")
            .scale(0.8f)
            .pos(5, bounds.getHeight() -10)
                .render(stack);
  }

}
