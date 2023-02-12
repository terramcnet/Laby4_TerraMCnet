package net.terramc.core;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class TerraNameTagConfiguration extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> showTag = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> showIconTag = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> showIconInTab = new ConfigProperty<>(true);

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

}
