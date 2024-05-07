package net.terramc.addon;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownEntryTranslationPrefix;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;
import net.terramc.addon.util.CustomTextDecoration;

@SpriteTexture("sprite/settings")
public class TerraNameTagConfiguration extends Config {

  @SettingSection(value = "general", center = true)

  @SpriteSlot(x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SpriteSlot()
  @SwitchSetting
  private final ConfigProperty<Boolean> showTag = new ConfigProperty<>(true);

  @SpriteSlot(y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> showIconTag = new ConfigProperty<>(true);

  @SpriteSlot(y = 1, x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> showIconInTab = new ConfigProperty<>(true);

  @SettingSection(value = "customization", center = true)

  @ColorPickerSetting(chroma = true)
  private final ConfigProperty<Color> nameTagColor = new ConfigProperty<>(Color.ofRGB(255, 255, 255));

  @DropdownSetting
  @DropdownEntryTranslationPrefix("terramc.settings.nameTagConfiguration.textDecoration")
  private final ConfigProperty<CustomTextDecoration> textDecoration = new ConfigProperty<>(CustomTextDecoration.NONE);

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> showTag() {
    return this.showTag;
  }

  public ConfigProperty<Boolean> showIconTag() {
    return this.showIconTag;
  }

  public ConfigProperty<Boolean> showIconInTab() {
    return this.showIconInTab;
  }

  public ConfigProperty<Color> nameTageColor() {
    return nameTagColor;
  }

  public ConfigProperty<CustomTextDecoration> textDecoration() {
    return textDecoration;
  }
}
