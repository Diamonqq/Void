package com.fejlip.features;

import com.fejlip.Macro;
import com.fejlip.config.Config;
import com.fejlip.helpers.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class AutoBuy {
    private int lastAuctionBought = 0;
    private boolean bedStarted = false;
    
    public Thread getBedThread() {
        return bedThread;
    }
    
    private Thread bedThread = null;

    private void handleBuyFinished() {
        bedStarted = false;
        if (bedThread != null && bedThread.isAlive()) {
            try {
                bedThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Macro.getInstance().getQueue().setRunning(false);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInventoryRendering(GuiScreenEvent.DrawScreenEvent.Post post) {
        Config config = Macro.getInstance().getConfig();
        if (config != null && config.isAutoBuyEnabled() && (post.gui instanceof GuiChest) ) {
            ContainerChest chest = (ContainerChest) ((GuiChest) post.gui).inventorySlots;
            if (chest != null) {
                String name = chest.getLowerChestInventory().getName();
                if (name.contains("BIN Auction View")) {
                    ItemStack stack = chest.getSlot(31).getStack();
                    if (stack != null) {
                        if (Items.feather != stack.getItem()) {
                            if (Items.potato == stack.getItem()) {
                                Helpers.sendDebugMessage("Someone bought the auction already, skipping...");
                                handleBuyFinished();
                                Minecraft.getMinecraft().thePlayer.closeScreen();
                            } else if (Items.bed == stack.getItem() && !bedStarted) {
                                bedStarted = true;
                                bedThread = new Thread(() -> {
                                    try {
                                        while (true) {
                                            if (chest.getLowerChestInventory().getName().contains("BIN Auction View")) {
                                            clickNugget(chest.windowId);
                                            clickConfirm(chest.windowId+1);
                                            Thread.sleep(Macro.getInstance().getConfig().getBedClickDelay());
                                        }}
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                });
                                bedThread.start();
                            } else if (Items.gold_nugget == stack.getItem() || Item.getItemFromBlock(Blocks.gold_block) == stack.getItem()) {
                                clickNugget(chest.windowId);
                                clickConfirm(chest.windowId+1);
                            }
                        }
                    }
                } else if (name.contains("Confirm Purchase")) {
                    if (chest.windowId != this.lastAuctionBought) {
                        clickConfirm(chest.windowId);
                        this.lastAuctionBought = chest.windowId;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientChatMessage(ClientChatReceivedEvent event) {
        String str = event.message.getUnformattedText();
        if (str.contains("This auction wasn't found") || str.contains("There was an error with the auction")) {
            Helpers.sendDebugMessage("Error or not found");
            handleBuyFinished();
        }
        if (str.contains("You don't have enough coins to afford this bid!")) {
            Helpers.sendDebugMessage("Not enough coins to buy this auction, skipping...");
            handleBuyFinished();
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
        if (str.contains("Putting coins in")) {
            handleBuyFinished();
            Macro.getInstance().getQueue().setRunning(false);
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }
    
    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) bedThread = null;
    }

    private void clickNugget(int id) {
        click(id, 31);
    }

    private void clickConfirm(int id) {
        click(id, 11);
    }

    private void click(int id, int index) {
        (Minecraft.getMinecraft()).playerController.windowClick(id, index, 0, 3, Minecraft.getMinecraft().thePlayer);
    }
}

