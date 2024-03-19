package net.terramc.addon;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.terramc.addon.data.AddonData;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
@SpriteTexture("sprite/settings")
public class TerraConfiguration extends AddonConfig {

  @SpriteSlot(x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  //@SpriteSlot(x = 4)
  //@SwitchSetting
  //private final ConfigProperty<Boolean> sendStatistics = new ConfigProperty<>(true);

  @SettingSection(value = "general", center = true)

  @SpriteSlot()
  public TerraNameTagConfiguration nameTagConfiguration = new TerraNameTagConfiguration();

  @SpriteSlot(x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> updateStatsOnJoin = new ConfigProperty<>(false);

  @SettingSection(value = "premium", center = true)

  @SpriteSlot(x = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> enableAutoGG = new ConfigProperty<>(true);



  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  /*public ConfigProperty<Boolean> sendStatistics() {
    return sendStatistics;
  }*/

  public ConfigProperty<Boolean> updateStatsOnJoin() {
    return this.updateStatsOnJoin;
  }

  public ConfigProperty<Boolean> enableAutoGG() {
    return this.enableAutoGG;
  }


  // Staff Settings

  private final ConfigProperty<AddonData.CloudNotifyType> cloudNotifyType = new ConfigProperty<>(AddonData.CloudNotifyType.CHAT);

  private final ConfigProperty<Boolean> showTagAlways = new ConfigProperty<>(false);

  public ConfigProperty<AddonData.CloudNotifyType> cloudNotifyType() {
    return cloudNotifyType;
  }

  public ConfigProperty<Boolean> showTagAlways() {
    return showTagAlways;
  }

  private final ConfigProperty<Boolean> hideOwnTag = new ConfigProperty<>(false);

  public ConfigProperty<Boolean> hideOwnTag() {
    return hideOwnTag;
  }
}
