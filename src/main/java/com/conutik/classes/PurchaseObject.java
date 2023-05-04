package com.conutik.classes;

import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import com.conutik.helpers.Utils;
import com.conutik.helpers.Webhook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.NumberFormat;

public class PurchaseObject {
    private final String itemName;
    private final String itemSoldFor;
    private final String itemSellingFor;
    private final String itemProfitPercentage;
    private ItemStack itemStack;
    private int itemSlot = 0;
    NumberFormat myFormat = NumberFormat.getInstance();

    public PurchaseObject(String itemName, String itemSoldFor, String itemSellingFor, String itemProfitPercent) {
        this.itemName = itemName;
        this.itemSoldFor = itemSoldFor;
        this.itemSellingFor = itemSellingFor;
        this.itemProfitPercentage = itemProfitPercent;
    }

    public void bought() {
        String content = "<@" + Settings.discordID + ">";
        String description = null;
        if(!Settings.flipWebhookDescription.isEmpty()) description = Settings.flipWebhookDescription;
        if(Settings.discordID.isEmpty()) content = "New flip found!";
        if(Settings.flipWebhookURL.isEmpty()) return;
        long purse = Utils.purse;
        long longValue = (long)Math.round(Double.parseDouble(this.itemSoldFor.toUpperCase().split("[KM]")[0]) * Long.parseLong(this.itemSoldFor.split("(?![0.0-9.0])")[1].toUpperCase().replaceAll("K", "1000").replaceAll("M", "1000000")));
        purse = (purse - longValue);
        Webhook webhook = new Webhook(Settings.flipWebhookURL);
        webhook.setContent(content);
        webhook.setAvatarUrl("https://cdn.discordapp.com/icons/1074429357642227853/f2f3c75446e8774afe5c448d83401153.png");
        webhook.setUsername("Void Flipper");
        webhook.addEmbed(
                new Webhook.EmbedObject()
                        .setTitle("Just purchased an item!")
                        .setColor(Settings.flipWebhookColor)
                        .addField("Item:", this.itemName, false)
                        .addField("Selling for:", this.itemSoldFor, false)
                        .addField("Projected Price:", this.itemSellingFor, false)
                        .addField("Percentage profit:", this.itemProfitPercentage + "%", false)
                        .setDescription(description)
                        .setFooter("Purse: " + myFormat.format(purse), "")
                        .setThumbnail("https://minotar.net/helm/" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "/600.png")
        );
        try{
            webhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
            Helpers.sendDebugMessage(e.getMessage());
        }
    }

    public void autoSell() {
        if(!Settings.autoSell) return;
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/ah");
        // Open bids
        Utils.setTimeout(() -> {
            if (!(Minecraft.getMinecraft().thePlayer.openContainer instanceof ContainerChest)) return;
            if (!((ContainerChest) Minecraft.getMinecraft().thePlayer.openContainer).getLowerChestInventory().getName().contains("Auction House"))
                return;
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            Minecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, 13, 0, 3, player);
            // Claim Bid
            Utils.setTimeout(() -> {
                ItemStack[] items = player.openContainer.getInventory().toArray(new ItemStack[0]);
                for (int i = 0; i < items.length; i++) {
                    ItemStack item = items[i];
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(item.getDisplayName()).equals(this.itemName)) {
                        Minecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, i, 0, 3, player);
                        // Get item and claim it
                        Utils.setTimeout(() -> {
                            ItemStack itemStack = Minecraft.getMinecraft().thePlayer.openContainer.getSlot(13).getStack();
                            Minecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, 31, 0, 3, player);
                            // Find item
                            Utils.setTimeout(() -> {
                                player.sendChatMessage("/ah");
                                try {
                                    Utils.setTimeout(() -> {
                                        int isal = 0;
                                        for (ItemStack s : Minecraft.getMinecraft().thePlayer.openContainer.getInventory()) {
                                            isal++;
                                            Helpers.sendDebugMessage(s.getDisplayName() + " : " + isal);
                                            if (itemSlot != 0) break;
                                            if (s.isItemEqual(itemStack)) {
                                                itemSlot = isal - 1;
                                                break;
                                            }
                                        }
                                        Helpers.sendDebugMessage(String.valueOf(itemSlot));
                                        Minecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, itemSlot, 0, 3, player);
                                        Utils.setTimeout(() -> {
                                            Minecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, 31, 0, 3, player);
                                            Utils.setTimeout(() -> {
                                                if (Minecraft.getMinecraft().currentScreen instanceof GuiEditSign) {
                                                    Field title = null;
                                                    try {
                                                        title = Minecraft.getMinecraft().currentScreen.getClass().getDeclaredField("tileSign");
                                                    } catch (NoSuchFieldException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    title.setAccessible(true);
                                                    try {
                                                        ((TileEntitySign) title.get(Minecraft.getMinecraft().currentScreen)).signText[0] = new ChatComponentText(this.itemSellingFor);
                                                        Utils.setTimeout(() -> {
                                                            Minecraft.getMinecraft().displayGuiScreen(null);
                                                            Utils.setTimeout(() -> {
                                                                Minecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, 29, 0, 3, player);
                                                                Utils.setTimeout(() -> {
                                                                    Minecraft.getMinecraft().playerController.windowClick(player.openContainer.windowId, 11, 0, 3, player);
                                                                }, 500);
                                                            }, 500);
                                                        }, 500);
                                                    } catch (IllegalAccessException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, 500);
                                        }, 500);
                                    }, 500);
                                } catch(NullPointerException e) {
                                    throw new NullPointerException(e.getMessage());
                                }
                            }, 500);
                        }, 500);
                    }
                }
            }, 500);
        }, 500);
    }
}


