package net.terramc.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
public class TerraConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  /*@SettingSection(value = "gui", center = true)

  @SwitchSetting
  private final ConfigProperty<Boolean> guiEnabled = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> guiEnabled() {
    return this.guiEnabled;
  }

  @KeyBindSetting
  private final ConfigProperty<Key> guiKey = new ConfigProperty<>(Key.MULTIPLY);*/

  @SettingSection(value = "general", center = true)

  public TerraNameTagConfiguration nameTagConfiguration = new TerraNameTagConfiguration();

  @SwitchSetting
  private final ConfigProperty<Boolean> updateStatsOnJoin = new ConfigProperty<>(false);

  public ConfigProperty<Boolean> updateStatsOnJoin() {
    return this.updateStatsOnJoin;
  }



  @SettingSection(value = "premium", center = true)

  @SwitchSetting
  private final ConfigProperty<Boolean> enableAutoGG = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> enableAutoGG() {
    return this.enableAutoGG;
  }


}
