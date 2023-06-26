package net.terramc.addon.util;

import net.labymod.api.client.component.format.TextDecoration;

public enum CustomTextDecoration {

  NONE(null),
  BOLD(TextDecoration.BOLD),
  STRIKETHROUGH(TextDecoration.STRIKETHROUGH),
  UNDERLINED(TextDecoration.UNDERLINED),
  ITALIC(TextDecoration.ITALIC);

  private final TextDecoration labyDecoration;

  CustomTextDecoration(TextDecoration labyDecoration) {
    this.labyDecoration = labyDecoration;
  }

  public TextDecoration getLabyDecoration() {
    return labyDecoration;
  }
}
