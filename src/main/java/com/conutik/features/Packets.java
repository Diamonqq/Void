package com.conutik.features;

import com.conutik.helpers.Helpers;
import gg.essential.universal.ChatColor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.awt.*;
@ChannelHandler.Sharable
public class Packets extends SimpleChannelInboundHandler<Packet> {
    private long startTime;
    private ChannelPipeline pipeline;


@SubscribeEvent
public void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
    pipeline = event.manager.channel().pipeline();
    pipeline.addBefore("packet_handler", this.getClass().getName(), this);
}

    @SubscribeEvent
    public void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        pipeline = null;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {
        // only listening for S2FPacketSetSlot packets!
        ctx.fireChannelRead(msg);
        if ((msg instanceof S2DPacketOpenWindow)) {
            S2DPacketOpenWindow packet = (S2DPacketOpenWindow) msg;
            if(packet.getWindowTitle().getUnformattedText().equals("BIN Auction View")){
                Helpers.sendDebugMessage("Started timer");
                this.startTime = System.currentTimeMillis();
            }
        } else if(msg instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) msg;
            if (packet.getChatComponent().getUnformattedText().replaceAll("§.", "").contains("Putting coins in escrow")) {
                long endTime = System.currentTimeMillis();
                Helpers.sendChatMessage(ChatColor.GREEN + "Auction bought in " + (endTime - startTime) + " ms");
            }
        }
    }


}
