package net.terramc.addon.terrachat.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.labymod.api.util.logging.Logging;
import net.terramc.addon.terrachat.TerraChatClient;
import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;

public class PacketEncoder extends MessageToByteEncoder<TerraPacket> {

  private final Logging LOGGER = Logging.getLogger();

    private final TerraChatClient chatClient;

    public PacketEncoder(TerraChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TerraPacket packet, ByteBuf byteBuf) {
      int packetId = this.chatClient.protocol().getPacketId(packet);

      if(packetId != 14 && packetId != 15) {
        LOGGER.debug("[MoneyChatClient] [OUT] " + packetId + " " + packet.getClass().getSimpleName());
      }

      TerraPacketBuffer packetBuffer = new TerraPacketBuffer(byteBuf);
      packetBuffer.writeVarIntToBuffer(packetId);
      packet.write(packetBuffer);
    }

}
