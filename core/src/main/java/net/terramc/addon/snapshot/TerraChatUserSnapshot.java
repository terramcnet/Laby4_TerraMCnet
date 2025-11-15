package net.terramc.addon.snapshot;

import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.util.TerraChatUser;
import org.jetbrains.annotations.Nullable;

public class TerraChatUserSnapshot extends AbstractLabySnapshot {

  private @Nullable TerraChatUser terraChatUser;

  public TerraChatUserSnapshot(Player player, Extras extras) {
    super(extras);
    this.terraChatUser = AddonData.getChatUsers().get(player.profile().getUniqueId());
  }

  public @Nullable TerraChatUser getTerraChatUser() {
    return terraChatUser;
  }

}
