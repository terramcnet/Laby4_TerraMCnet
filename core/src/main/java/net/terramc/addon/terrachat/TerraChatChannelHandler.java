package net.terramc.addon.terrachat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.terramc.addon.terrachat.pipeline.PacketDecoder;
import net.terramc.addon.terrachat.pipeline.PacketEncoder;
import net.terramc.addon.terrachat.pipeline.PacketPrepender;
import net.terramc.addon.terrachat.pipeline.PacketSplitter;
import net.terramc.addon.terrachat.protocol.TerraPacketHandler;
import java.util.concurrent.TimeUnit;

public class TerraChatChannelHandler extends ChannelInitializer<NioSocketChannel> {

  private TerraChatClient moneyChatClient;
  private final TerraPacketHandler packetHandler;
  private NioSocketChannel channel;

  public TerraChatChannelHandler(TerraChatClient moneyChatClient, TerraPacketHandler packetHandler) {
    this.moneyChatClient = moneyChatClient;
    this.packetHandler = packetHandler;
  }

  public void initChannel(NioSocketChannel channel) {
    this.channel = channel;
    channel.pipeline()
        .addLast("timeout", new ReadTimeoutHandler(30L, TimeUnit.SECONDS))
        .addLast("splitter", new PacketSplitter())
        .addLast("decoder", new PacketDecoder(this.moneyChatClient))
        .addLast("prepender", new PacketPrepender())
        .addLast("encoder", new PacketEncoder(this.moneyChatClient))
        .addLast(this.packetHandler);
  }

  public NioSocketChannel getChannel() {
    return channel;
  }
}
