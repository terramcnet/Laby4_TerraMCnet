package net.terramc.addon.gui.activity;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.I18n;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.StaffSettings;
import net.terramc.addon.util.ActivityUtil;
import net.terramc.addon.util.CloudNotifyType;

@AutoActivity
public class TerraStaffActivity extends Activity {

  private TerraAddon addon;

  public TerraStaffActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.rankUtil().isStaff()) return;

    DropdownWidget<CloudNotifyType> cloudNotify = DropdownWidget.create(StaffSettings.getCloudNotifyType(),
        value -> {
          StaffSettings.setCloudNotifyType(value);
          this.addon.apiUtil().sendStaffSetting(Laby.references().gameUserService().clientGameUser()
              .getUniqueId(), "CloudNotify", value.getName());
        });
    cloudNotify.translationKeyPrefix("terramc.ui.staff.cloudNotify");
    cloudNotify.addAll(CloudNotifyType.values());
    cloudNotify.top().set(20.0F);
    cloudNotify.left().set(50.0F);

    this.document.addChild(cloudNotify);

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);

    Bounds bounds = this.bounds();
    TextRenderer textRenderer = this.labyAPI.renderPipeline().textRenderer();

    ActivityUtil.drawCredits(textRenderer, bounds, stack);

    if(!this.addon.rankUtil().isStaff()) {
      textRenderer.text(I18n.translate("terramc.ui.general.noAccess"))
          .pos(bounds.getCenterX(), bounds.getCenterY())
          .centered(true)
          .render(stack);
      return;
    }

    drawString(textRenderer, stack, I18n.translate("terramc.ui.staff.cloudNotify.title"), 50F, 10F);

  }

  private void drawString(TextRenderer renderer, Stack stack, String text, float x, float y) {
    renderer.text(text).pos(x, y).render(stack);
  }

}
