package net.terramc.addon.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.LabyConnectBroadcastEvent;
import net.labymod.api.event.labymod.labyconnect.session.LabyConnectBroadcastEvent.Action;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.data.AddonData;

public class BroadcastListener {

  private TerraAddon addon;

  public BroadcastListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onBroadcastReceive(LabyConnectBroadcastEvent event) {
    if(event.action() != Action.RECEIVE) return;
    if(!event.getKey().equals("terra-group-tag")) return;
    JsonElement payload = event.getPayload();
    if(!payload.isJsonObject()) return;
    JsonObject object = payload.getAsJsonObject();

    this.addon.logger().info(object.toString());
    this.addon.displayMessage(object.toString());

    if(object.has("hideTag") && object.get("hideTag").isJsonObject()) {
      JsonObject data = object.get("hideTag").getAsJsonObject();
      UUID uuid = UUID.fromString(data.get("uuid").getAsString());
      if(!AddonData.getShouldHideTag().contains(uuid)) {
        AddonData.getShouldHideTag().add(uuid);
        if(this.addon.rankUtil().isAdmin()) {
          this.addon.pushNotification(Component.translatable("terramc.notification.success.title"),
              Component.translatable("terramc.notification.other.tag-disabled", Component.text(data.get("userName").getAsString()).
                      color(TextColor.color(255, 85, 85))).
                  color(TextColor.color(170, 170, 170)));
        }
      }
    }

    if(object.has("showTag") && object.get("showTag").isJsonObject()) {
      JsonObject data = object.get("showTag").getAsJsonObject();
      UUID uuid = UUID.fromString(data.get("uuid").getAsString());
      if(AddonData.getShouldHideTag().contains(uuid)) {
        AddonData.getShouldHideTag().remove(uuid);
        if(this.addon.rankUtil().isAdmin()) {
          this.addon.pushNotification(Component.translatable("terramc.notification.success.title"),
              Component.translatable("terramc.notification.other.tag-enabled", Component.text(data.get("userName").getAsString()).
                      color(TextColor.color(85, 255, 85))).
                  color(TextColor.color(170, 170, 170)));
        }
      }
    }

    if(object.has("usingAddon")) {
        UUID uuid = UUID.fromString(object.get("usingAddon").getAsString());
        if(!AddonData.getUsingAddon().contains(uuid)) {
          AddonData.getUsingAddon().add(uuid);
        }
      }

  }

}
