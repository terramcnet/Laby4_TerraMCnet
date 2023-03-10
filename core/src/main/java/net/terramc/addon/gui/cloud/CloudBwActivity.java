package net.terramc.addon.gui.cloud;

import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.util.ActivityUtil;

@AutoActivity
@Link("cloud.lss")
public class CloudBwActivity extends Activity {

  private TerraAddon addon;

  public CloudBwActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.rankUtil().canControlCloud()) return;

    List<ButtonWidget> buttonList = new ArrayList<>();

    // 4x1
    ButtonWidget re4x1 = ButtonWidget.text("§c4x1 §7- " + CloudMainActivity.restart);
    re4x1.addId("left-first");
    re4x1.addId("top-first-b1");
    re4x1.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "BW-4x1"));
    buttonList.add(re4x1);

    ButtonWidget ma4x1 = ButtonWidget.text("§c4x1 §7- " + CloudMainActivity.maintenance);
    ma4x1.addId("left-first");
    ma4x1.addId("top-first-b2");
    ma4x1.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "BW-4x1"));
    buttonList.add(ma4x1);

    // 4x2
    ButtonWidget re4x2 = ButtonWidget.text("§c4x2 §7- " + CloudMainActivity.restart);
    re4x2.addId("left-first");
    re4x2.addId("top-second-b1");
    re4x2.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "BW-4x2"));
    buttonList.add(re4x2);

    ButtonWidget ma4x2 = ButtonWidget.text("§c4x2 §7- " + CloudMainActivity.maintenance);
    ma4x2.addId("left-first");
    ma4x2.addId("top-second-b2");
    ma4x2.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "BW-4x2"));
    buttonList.add(ma4x2);

    // 4x4
    ButtonWidget re4x4 = ButtonWidget.text("§c4x4 §7- " + CloudMainActivity.restart);
    re4x4.addId("left-first");
    re4x4.addId("top-third-b1");
    re4x4.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "BW-4x4"));
    buttonList.add(re4x4);

    ButtonWidget ma4x4 = ButtonWidget.text("§c4x4 §7- " + CloudMainActivity.maintenance);
    ma4x4.addId("left-first");
    ma4x4.addId("top-third-b2");
    ma4x4.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "BW-4x4"));
    buttonList.add(ma4x4);

    // 8x1
    ButtonWidget re8x1 = ButtonWidget.text("§c8x1 §7- " + CloudMainActivity.restart);
    re8x1.addId("left-second");
    re8x1.addId("top-first-b1");
    re8x1.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "BW-8x1"));
    buttonList.add(re8x1);

    ButtonWidget ma8x1 = ButtonWidget.text("§c8x1 §7- " + CloudMainActivity.maintenance);
    ma8x1.addId("left-second");
    ma8x1.addId("top-first-b2");
    ma8x1.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "BW-8x1"));
    buttonList.add(ma8x1);

    // 2x2
    ButtonWidget re2x2 = ButtonWidget.text("§c2x2 §7- " + CloudMainActivity.restart);
    re2x2.addId("left-second");
    re2x2.addId("top-second-b1");
    re2x2.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "BW-2x2"));
    buttonList.add(re2x2);

    ButtonWidget ma2x2 = ButtonWidget.text("§c2x2 §7- " + CloudMainActivity.maintenance);
    ma2x2.addId("left-second");
    ma2x2.addId("top-second-b2");
    ma2x2.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "BW-2x2"));
    buttonList.add(ma2x2);

    buttonList.forEach(this.document::addChild);

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);

    Bounds bounds = this.bounds();
    TextRenderer textRenderer = this.labyAPI.renderPipeline().textRenderer();

    ActivityUtil.drawCredits(textRenderer, bounds, stack);

    /*if(!TerraAddon.isConnectedTerra()) {
      textRenderer.text(I18n.translate("terramc.ui.general.noInfoAvailable"))
          .pos(bounds.getCenterX(), bounds.getCenterY())
          .centered(true)
          .render(stack);

      textRenderer.text(I18n.translate("terramc.ui.general.connectToServer"))
          .pos(bounds.getCenterX(), bounds.getCenterY() + 10)
          .centered(true)
          .render(stack);
    }*/

  }

}
