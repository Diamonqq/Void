package com.conutik.features;

import com.conutik.Macro;
import com.conutik.classes.QueueItem;
import com.conutik.config.Settings;
import com.conutik.helpers.*;
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
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static com.conutik.helpers.Utils.ScoreboardData;
import static com.conutik.helpers.Utils.setTimeout;

public class AutoBuy {
    private int lastAuctionBought = 0;
    private static boolean bedStarted = false;

    public static Thread bedThread = null;
    public final ExecutorService tickThreadPool = Executors.newFixedThreadPool(2);

    private final FlipperMessage flipper = new FlipperMessage();
    private String lastCommandRan;

    private Queue queue = Macro.getInstance().getQueue();

    Pattern percentageProfitRegexValue = Pattern.compile("\\\\d+(?:\\\\.\\\\d+)?(?=\\\\%)");
    Pattern priceRegex = Pattern.compile("\\d+(?:\\.\\d+)?(?=[MKB])");

    public static void handleBuyFinished() {
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
        if(Settings.autoBuy && (post.gui instanceof GuiChest)) {
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
                                    int loopInt = 0;
                                    try {
                                        while (loopInt < 100) {
                                            loopInt = loopInt + 1;
                                            if (chest.getLowerChestInventory().getName().contains("BIN Auction View")) {
                                                clickNugget(chest.windowId);
                                                clickConfirm(chest.windowId+1);
                                                Thread.sleep(Settings.bedDelay);
                                            }}
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                });
                                bedThread.start();
                                setTimeout(AutoBuy::handleBuyFinished, 5000);
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
        if(event.type == 2) return;
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
        if (str.contains("You purchased ") && !Settings.flipWebhookURL.isEmpty()) {
            Helpers.sendDebugMessage("Purchased an item");
            try {
                flipper.itemBought(str);
            } catch(IOException e) {
                Helpers.sendDebugMessage("An error occured while trying to send webhook.");
            }
        }
        if(str.equals("You were spawned in Limbo.") && Settings.antiLimbo) {
            Helpers.sendDebugMessage("Found you in, taking you back to lobby");
            Utils.setTimeout(() -> {
                lastCommandRan = "/lobby";
                (Minecraft.getMinecraft()).thePlayer.sendChatMessage("/lobby");
                if(Settings.autoLobby) Utils.setTimeout(() -> {
                    lastCommandRan = "/play sb";
                    Helpers.sendDebugMessage("You in the lobby not skyblock, let me change that rq");
                    (Minecraft.getMinecraft()).thePlayer.sendChatMessage("/play sb");
                    if(Settings.autoIsland) Utils.setTimeout(() -> {
                        lastCommandRan = "/is";
                        Helpers.sendDebugMessage("Kicking you back to your minions");
                        (Minecraft.getMinecraft()).thePlayer.sendChatMessage("/is");
                    }, 5000);
                }, 2500);
            }, 2500);
        }

        if(str.contains("Cannot join SkyBlock for a moment!") && Settings.autoLobby) {
            Helpers.sendDebugMessage("Trying to push you into skyblock again");
            Utils.setTimeout(() -> {
                lastCommandRan = "/play sb";
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play sb");
            }, 2500);
        }
        if(str.contains("You are sending commands too fast!") && Settings.autoLobby) {
            Helpers.sendDebugMessage("Running commands quickly? slow down mcqueen");
            Utils.setTimeout(() -> {
                if(lastCommandRan != null) Minecraft.getMinecraft().thePlayer.sendChatMessage(lastCommandRan);
            }, 2500);
        }

        if(str.replaceAll("§.", "").contains("Hello there, you acted suspiciously like a macro bot")) {
            String content = "<@" + Settings.discordID + ">";
            if(!Settings.captchaWebhooks) return;
            if(Settings.discordID.isEmpty()) content = "A Captcha appeared";
            if(Settings.captchaWebhookURL.isEmpty()) return;
            String description = null;
            if(!Settings.captchaWebhookDescription.isEmpty()) description = Settings.captchaWebhookDescription;
            Webhook webhook = new Webhook(Settings.captchaWebhookURL);
            webhook.setContent(content);
            webhook.setAvatarUrl("https://cdn.discordapp.com/icons/1074429357642227853/f2f3c75446e8774afe5c448d83401153.png");
            webhook.setUsername("Void Flipper");
            webhook.addEmbed(
                    new Webhook.EmbedObject()
                            .setTitle("Captcha has appeared")
                            .setColor(Color.BLACK)
                            .setDescription("```A WILD CAPTCHA HAS APPEARED. SOLVE IT TO CONTINUE YOUR FLIPPING JOURNEY !!!```\n" + description)
                            .setThumbnail("https://minotar.net/helm/" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "/600.png")
            );
            try{
                webhook.execute();
            } catch (IOException e) {
                Helpers.sendDebugMessage(e.getMessage());
            }
        }

        if(str.contains("[Auction]") && str.contains("bought") && !event.message.getChatStyle().isEmpty()) {
            String action = event.message.getChatStyle().getChatClickEvent().getValue();
            if(Settings.autoClaim) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage(action);
                Utils.setTimeout(() -> {
                    Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.openContainer.windowId, 31, 0, 3, Minecraft.getMinecraft().thePlayer);
                    Helpers.sendDebugMessage("Auto claimed auction :D");
                }, 1000);
            }
            if(!Settings.purchaseWebhooks) return;
            str = str.replaceAll("§.", "");
            String purchaser = str.split("\\[Auction] ")[1].split("bought")[0];
            String price = str.split("for ")[1].split("coins")[0];
            String item = str.split("bought ")[1].split("for")[0];
            String content = "<@" + Settings.discordID + ">";
            if(Settings.discordID.isEmpty()) content = "Flip sold";
            if(Settings.purchaseWebhookURL.isEmpty()) return;
            String description = null;
            if(!Settings.purchaseWebhookDescription.isEmpty()) description = Settings.purchaseWebhookDescription;
            Webhook purchaseWebhook = new Webhook(Settings.purchaseWebhookURL);
            Helpers.sendDebugMessage(purchaser);
            Helpers.sendDebugMessage(item);
            Helpers.sendDebugMessage(price);
            purchaseWebhook.setContent(content);
            purchaseWebhook.setAvatarUrl("https://cdn.discordapp.com/icons/1074429357642227853/f2f3c75446e8774afe5c448d83401153.png");
            purchaseWebhook.setUsername("Void Flipper");
            purchaseWebhook.addEmbed(
                    new Webhook.EmbedObject()
                            .setTitle("Item Sold")
                            .setColor(Color.BLACK)
                            .addField("Item:", item, false)
                            .addField("Purchaser:", purchaser, false)
                            .addField("Price:", price, false)
                            .setDescription(description)
                            .setThumbnail("https://minotar.net/helm/" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "/600.png")
            );
            try{
                purchaseWebhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
                Helpers.sendDebugMessage(e.toString());
            }
        }
    }



    long UpdateThisTick = 0;
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onTick(TickEvent.ClientTickEvent event) {
        UpdateThisTick++;
        if (UpdateThisTick % 200 == 0) {
            tickThreadPool.submit(() -> {
                try {
                    ScoreboardData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        if (Settings.autoOpen && !queue.isEmpty() && !queue.isRunning()) {
            this.queue.setRunning(true);
            QueueItem item = this.queue.get();
            item.openAuction();
        }
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

