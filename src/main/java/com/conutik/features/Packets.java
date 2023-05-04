package com.conutik.features;

import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import gg.essential.universal.ChatColor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

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
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws InterruptedException {
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
        } else if(msg instanceof S2FPacketSetSlot) {
//            if(!Settings.autoBuy) return;
//            S2FPacketSetSlot packet = (S2FPacketSetSlot) msg;
//            ItemStack item = packet.func_149174_e();
//            if (packet.func_149174_e() == null) {
//                return;
//            }
//            if(!(Minecraft.getMinecraft().thePlayer.openContainer instanceof ContainerChest)) return;
//            if(!((ContainerChest) Minecraft.getMinecraft().thePlayer.openContainer).getLowerChestInventory().getName().contains("BIN Auction View")) return;
//            if((Minecraft.getMinecraft().thePlayer.openContainer.getInventory().size()-36) < 32) return;
//            if(Minecraft.getMinecraft().thePlayer.openContainer.getInventory().get(31) != null && Minecraft.getMinecraft().thePlayer.openContainer.getInventory().get(31).getItem() != Items.bed) return;
//            NBTTagCompound tag = item.getTagCompound();
//            NBTTagCompound ea = tag.getCompoundTag("display");
//            NBTTagList lore = ea.getTagList("Lore", 8);
//                String ms = lore.getStringTagAt(lore.tagCount() - 1);
//                if(EnumChatFormatting.getTextWithoutFormattingCodes(ms).startsWith("Ends in")) {
//                    for(int is = 0; is<1; is++) {
//                        (Minecraft.getMinecraft()).playerController.windowClick(packet.func_149175_c(), 31, 0, 3, Minecraft.getMinecraft().thePlayer);
//                        (Minecraft.getMinecraft()).playerController.windowClick(packet.func_149175_c()+1, 11, 0, 3, Minecraft.getMinecraft().thePlayer);
//                        Thread.sleep(1);
//                    }
//            }
        }
    }


}
