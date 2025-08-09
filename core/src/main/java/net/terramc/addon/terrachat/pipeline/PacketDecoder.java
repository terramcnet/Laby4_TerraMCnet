package net.terramc.addon.terrachat.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import net.labymod.api.util.logging.Logging;
import net.terramc.addon.terrachat.TerraChatClient;
import net.terramc.addon.terrachat.protocol.TerraPacket;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;

public class PacketDecoder extends ByteToMessageDecoder {

  private final Logging LOGGER = Logging.getLogger();

    private final TerraChatClient chatClient;

    public PacketDecoder(TerraChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 1) {
            TerraPacketBuffer packetBuffer = new TerraPacketBuffer(byteBuf);
            int id = packetBuffer.readVarIntFromBuffer();
            TerraPacket packet = this.chatClient.protocol().getPacket(id);

            if(id != 14 && id != 15) {
              LOGGER.debug("[MoneyChatClient] [IN] " + id + " " + packet.getClass().getSimpleName());
            }

            packet.read(packetBuffer);
            if (byteBuf.readableBytes() > 0) {
              String simpleName = packet.getClass().getSimpleName();
              throw new IOException("Packet " + simpleName + " was larger than I expected, found " + byteBuf.readableBytes() + " bytes extra whilst reading packet " + packet);
            } else {
              list.add(packet);
            }

        }
    }
}
