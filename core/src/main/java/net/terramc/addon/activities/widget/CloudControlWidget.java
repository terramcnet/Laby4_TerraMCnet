package net.terramc.addon.activities.widget;

import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.terramc.addon.TerraAddon;
import java.util.UUID;

public class CloudControlWidget extends SimpleWidget {

  private String restartText = "§cNEUSTART";
  private String maintenanceText = "§4WARTUNG";

  private TerraAddon addon;
  private UUID uuid;
  private String groupName;
  private String groupDisplay;
  private boolean maintenance;

  public CloudControlWidget(TerraAddon addon, UUID uuid, String groupName, String groupDisplay, boolean maintenance) {
    this.addon = addon;
    this.uuid = uuid;
    this.groupName = groupName;
    this.groupDisplay = groupDisplay;
    this.maintenance = maintenance;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    DivWidget container = new DivWidget();
    container.addId("container");

    ComponentWidget title = ComponentWidget.text(this.groupDisplay);
    title.addId("title");

    container.addChild(title);

    ButtonWidget restartButton = ButtonWidget.text(this.restartText);
    restartButton.addId("restartBtn");
    restartButton.setActionListener(() -> this.addon.apiUtil().sendCloudControl(this.uuid, "restart", this.groupName));
    //addChild(restartButton);
    container.addChild(restartButton);

    if(this.maintenance) {
      ButtonWidget maintenanceButton = ButtonWidget.text(this.maintenanceText);
      maintenanceButton.addId("maintenanceBtn");
      maintenanceButton.setActionListener(() -> this.addon.apiUtil().sendCloudControl(this.uuid, "maintenance", this.groupName));
      //addChild(maintenanceButton);
      container.addChild(maintenanceButton);
    }

    addChild(container);

  }

}