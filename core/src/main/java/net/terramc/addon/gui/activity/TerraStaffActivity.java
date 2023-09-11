package net.terramc.addon.gui.activity;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.I18n;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.Util;

@AutoActivity
@Link("staff.lss")
public class TerraStaffActivity extends Activity {

  private TerraAddon addon;

  public TerraStaffActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.rankUtil().isStaff()) return;

    ComponentWidget cloudNotifyTitle = ComponentWidget.i18n("terramc.ui.staff.cloudNotify.title");
    cloudNotifyTitle.addId("cloudNotify-title");

    this.document.addChild(cloudNotifyTitle);

    DropdownWidget<AddonData.CloudNotifyType> cloudNotify = DropdownWidget.create(this.addon.configuration().cloudNotifyType().get(),
        value -> {
          this.addon.configuration().cloudNotifyType().set(value);
          this.addon.pushNotification(Component.text(TerraAddon.doubleLine + " ").append(Component.translatable("terramc.ui.staff.settings.title")),
          Component.translatable("terramc.ui.staff.settings.change",
              Component.translatable("terramc.ui.staff.cloudNotify.title").color(TextColor.color(255, 255, 85)),
              Component.translatable("terramc.ui.staff.cloudNotify."+value.name().toLowerCase()).color(TextColor.color(255, 170, 0)))
          .color(TextColor.color(170, 170, 170)));
        });
    cloudNotify.setTranslationKeyPrefix("terramc.ui.staff.cloudNotify");
    cloudNotify.addAll(AddonData.CloudNotifyType.values());
    cloudNotify.setHoverComponent(Component.translatable("terramc.ui.staff.cloudNotify.description").color(TextColor.color(170, 170, 170)));
    cloudNotify.addId("cloudNotify");

    this.document.addChild(cloudNotify);

    if(this.addon.rankUtil().isAdmin()) {

      ComponentWidget showTagAlwaysTitle = ComponentWidget.i18n("terramc.ui.staff.showTagAlways.title");
      showTagAlwaysTitle.addId("showTagAlways-title");

      this.document.addChild(showTagAlwaysTitle);

      SwitchWidget showTagAlways = SwitchWidget.create(value -> {
        this.addon.configuration().showTagAlways().set(value);
        this.addon.pushNotification(Component.text(TerraAddon.doubleLine + " ").append(Component.translatable("terramc.ui.staff.settings.title")),
            Component.translatable("terramc.ui.staff.settings.change",
                Component.translatable("terramc.ui.staff.showTagAlways.title").color(TextColor.color(255, 255, 85)),
                Component.translatable("terramc.ui.staff.settings." + (value ? "enabled" : "disabled")))
            .color(TextColor.color(170, 170, 170)));
      });
      showTagAlways.setValue(this.addon.configuration().showTagAlways().get());
      showTagAlways.setHoverComponent(Component.translatable("terramc.ui.staff.showTagAlways.description").color(
          TextColor.color(170, 170, 170)));
      showTagAlways.addId("showTagAlways");

      this.document.addChild(showTagAlways);

    }

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);

    Bounds bounds = this.bounds();
    TextRenderer textRenderer = this.labyAPI.renderPipeline().textRenderer();

    Util.drawCredits(this.labyAPI, bounds, stack);

    if(!this.addon.rankUtil().isStaff()) {
      textRenderer.text(I18n.translate("terramc.ui.general.noAccess"))
          .pos(bounds.getCenterX(), bounds.getCenterY())
          .centered(true)
          .render(stack);
    }

  }

}
