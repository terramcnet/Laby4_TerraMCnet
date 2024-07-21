package net.terramc.addon;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
@SpriteTexture("sprite/settings")
public class TerraConfiguration extends AddonConfig {

  @SpriteSlot(x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection(value = "general", center = true)

  @SpriteSlot()
  public TerraNameTagConfiguration nameTagConfiguration = new TerraNameTagConfiguration();

  @SpriteSlot(x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> updateStatsOnJoin = new ConfigProperty<>(false);

  @IntroducedIn(namespace = "terramc", value = "1.4.4")
  @SpriteSlot(x = 4)
  @KeyBindSetting
  private final ConfigProperty<Key> uiHotKey = new ConfigProperty<>(Key.NONE);

  @SettingSection(value = "premium", center = true)

  @SpriteSlot(x = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> enableAutoGG = new ConfigProperty<>(true);


  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> updateStatsOnJoin() {
    return this.updateStatsOnJoin;
  }

  public ConfigProperty<Key> uiHotKey() {
    return uiHotKey;
  }

  public ConfigProperty<Boolean> enableAutoGG() {
    return this.enableAutoGG;
  }

}
