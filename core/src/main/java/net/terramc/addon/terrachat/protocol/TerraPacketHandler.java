package net.terramc.addon.terrachat.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.terramc.addon.terrachat.protocol.packets.PacketPlayerStatus;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketAddonMessage;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketDisconnect;
import net.terramc.addon.terrachat.protocol.packets.TerraPacketPing;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketEncryptionRequest;
import net.terramc.addon.terrachat.protocol.packets.auth.TerraPacketLoginComplete;

public abstract class TerraPacketHandler extends SimpleChannelInboundHandler<TerraPacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, TerraPacket packet) {
    this.handlePacket(packet);
  }

  protected void handlePacket(TerraPacket packet) {
    packet.handle(this);
  }

  public abstract void handle(TerraPacketEncryptionRequest packet);
  public abstract void handle(TerraPacketLoginComplete packet);
  public abstract void handle(PacketPlayerStatus packet);
  public abstract void handle(TerraPacketDisconnect packet);
  public abstract void handle(TerraPacketPing packet);
  public abstract void handle(TerraPacketAddonMessage packet);

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();
  }



}
