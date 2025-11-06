package net.terramc.addon.snapshot;

import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.laby3d.renderer.snapshot.LabySnapshotFactory;
import net.labymod.api.service.annotation.AutoService;

@AutoService(LabySnapshotFactory.class)
public class TerraChatUserSnapshotFactory extends LabySnapshotFactory<Player, TerraChatUserSnapshot> {

  public TerraChatUserSnapshotFactory() {
    super(TerraAddonKeys.TERRA_CHAT_USER);
  }

  @Override
  protected TerraChatUserSnapshot create(Player player, Extras extras) {
    return new TerraChatUserSnapshot(player, extras);
  }
}
