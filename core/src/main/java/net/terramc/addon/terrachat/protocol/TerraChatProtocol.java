package net.terramc.addon.terrachat.protocol;

import java.util.HashMap;
import java.util.Map;
import net.labymod.api.util.logging.Logging;
import net.terramc.addon.terrachat.protocol.packets.PacketAddonStatistics;
import net.terramc.addon.terrachat.protocol.packets.PacketPlayerStatus;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketAddonMessage;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketDisconnect;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketPing;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketPong;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketEncryptionRequest;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketEncryptionResponse;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketLogin;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketLoginComplete;

public class TerraChatProtocol {

  private final Logging LOGGER = Logging.getLogger();

    private final Map<Integer, Class<? extends TerraPacket>> packets = new HashMap<>();

    public TerraChatProtocol() {
      register(0, TerraPacketLogin.class); // C -> S
      register(1, TerraPacketEncryptionRequest.class); // S -> C
      register(2, TerraPacketEncryptionResponse.class); // C -> S
      register(3, TerraPacketLoginComplete.class); // S -> C

      register(10, PacketPlayerStatus.class); // C <-> S
      register(11, PacketAddonStatistics.class); // C -> S
      register(12, TerraPacketDisconnect.class); // C <-> S
      register(14, TerraPacketPing.class); // S -> C
      register(15, TerraPacketPong.class); // C -> S

      register(20, TerraPacketAddonMessage.class); // C <-> S
    }

    private void register(int id, Class<? extends TerraPacket> clazz) {
        try {
            LOGGER.debug("Registering packet {} as id {}", clazz.getName(), id);
            packets.put(id, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TerraPacket getPacket(int id) throws Exception {
        if (!packets.containsKey(id)) {
            throw new RuntimeException("Packet with id " + id + " is not registered.");
        } else {
            return this.packets.get(id).getConstructor().newInstance();
        }
    }

    public int getPacketId(TerraPacket packet) {
        for (Map.Entry<Integer, Class<? extends TerraPacket>> entry : packets.entrySet()) {
            Class<? extends TerraPacket> value = entry.getValue();
            if (value.isInstance(packet)) {
                return entry.getKey();
            }
        }

        throw new RuntimeException("Packet " + packet + " is not registered.");
    }

}
