package net.terramc.addon.snapshot;

import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.group.Group;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class TerraUserSnapshot extends AbstractLabySnapshot {

  private UUID uuid;
  private @Nullable Group group;

  public TerraUserSnapshot(Player player, Extras extras) {
    super(extras);
    this.uuid = player.getUniqueId();
    this.group = AddonData.getStaffRankMap().getOrDefault(player.getUniqueId(), null);
  }

  public UUID getUuid() {
    return uuid;
  }

  @Nullable
  public Group getGroup() {
    return group;
  }

}
