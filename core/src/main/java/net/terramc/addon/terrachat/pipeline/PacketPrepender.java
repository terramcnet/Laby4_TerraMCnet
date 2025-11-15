package net.terramc.addon.terrachat.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.terramc.addon.terrachat.protocol.TerraPacketBuffer;

public class PacketPrepender extends MessageToByteEncoder<ByteBuf> {

  protected void encode(ChannelHandlerContext ctx, ByteBuf buffer, ByteBuf byteBuf) {
    int var4 = buffer.readableBytes();
    int var5 = TerraPacketBuffer.getVarIntSize(var4);
    if (var5 > 3) {
      throw new IllegalArgumentException("unable to fit " + var4 + " into 3");
    } else {
      byteBuf.ensureWritable(var5 + var4);
      TerraPacketBuffer.writeVarIntToBuffer(byteBuf, var4);
      byteBuf.writeBytes(buffer, buffer.readerIndex(), var4);
    }
  }

}
