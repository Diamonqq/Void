package com.conutik.features;

import java.util.ArrayList;
import java.util.List;

import com.conutik.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EnchantMacro {

    private static int until = 0;

    private static int tickAmount = 0;

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static Slot[] clickInOrderSlots = new Slot[36];

    private static int lastChronomatronRound = 0;

    private static final List<String> chronomatronPattern = new ArrayList<>();

    private static int chronomatronMouseClicks = 0;

    private static int lastUltraSequencerClicked = 0;

    private static long lastInteractTime = 0L;

    @SubscribeEvent
    public void onTick(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (mc.currentScreen instanceof GuiChest) {
            GuiChest inventory = (GuiChest) event.gui;
            Container containerChest = inventory.inventorySlots;
            if (containerChest instanceof ContainerChest) {
                List<Slot> invSlots = containerChest.inventorySlots;
                String invName = ((ContainerChest) containerChest).getLowerChestInventory().getDisplayName().getUnformattedText().trim();
                if (Settings.autoEnchant && invName.startsWith("Chronomatron (")) {
                    EntityPlayerSP player = mc.thePlayer;
                    if (player.inventory.getItemStack() == null && invSlots.size() > 48 && ((Slot) invSlots.get(49)).getStack() != null)
                        if (((Slot) invSlots.get(49)).getStack().getDisplayName().startsWith("§7Timer: §a") && ((Slot) invSlots.get(4)).getStack() != null) {
                            int round = (((Slot) invSlots.get(4)).getStack()).stackSize;
                            int timerSeconds = Integer.parseInt(StringUtils.stripControlCodes(((Slot) invSlots.get(49)).getStack().getDisplayName()).replaceAll("[^\\d]", ""));
                            if (round != lastChronomatronRound && timerSeconds == round + 2) {
                                lastChronomatronRound = round;
                                for (int i = 10; i <= 43; i++) {
                                    ItemStack stack = ((Slot) invSlots.get(i)).getStack();
                                    if (stack != null &&
                                            stack.getItem() == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
                                        chronomatronPattern.add(stack.getDisplayName());
                                        break;
                                    }
                                }
                            }
                            if (chronomatronMouseClicks < chronomatronPattern.size() && player.inventory.getItemStack() == null)
                                for (int i = 10; i <= 43; i++) {
                                    ItemStack glass = ((Slot) invSlots.get(i)).getStack();
                                    if (glass != null && player.inventory.getItemStack() == null && tickAmount % 5 == 0) {
                                        Slot glassSlot = invSlots.get(i);
                                        if (glass.getDisplayName().equals(chronomatronPattern.get(chronomatronMouseClicks))) {
                                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, glassSlot.slotNumber, 0, 0, (EntityPlayer) mc.thePlayer);
                                            lastInteractTime = 0L;
                                            chronomatronMouseClicks++;
                                            break;
                                        }
                                    }
                                }
                        } else if (((Slot) invSlots.get(49)).getStack().getDisplayName().equals("§aRemember the pattern!")) {
                            chronomatronMouseClicks = 0;
                        }
                }
                if (Settings.autoEnchant && invName.startsWith("Ultrasequencer (")) {
                    EntityPlayerSP player = mc.thePlayer;
                    if (invSlots.size() > 48 && ((Slot) invSlots.get(49)).getStack() != null && player.inventory.getItemStack() == null && ((Slot) invSlots.get(49)).getStack().getDisplayName().startsWith("§7Timer: §a")) {
                        lastUltraSequencerClicked = 0;
                        for (Slot slot4 : clickInOrderSlots) {
                            if (slot4 != null && slot4.getStack() != null && StringUtils.stripControlCodes(slot4.getStack().getDisplayName()).matches("\\d+")) {
                                int number = Integer.parseInt(StringUtils.stripControlCodes(slot4.getStack().getDisplayName()));
                                if (number > lastUltraSequencerClicked)
                                    lastUltraSequencerClicked = number;
                            }
                        }
                        if (clickInOrderSlots[lastUltraSequencerClicked] != null && player.inventory.getItemStack() == null && tickAmount % 2 == 0 && lastUltraSequencerClicked != 0 && until == lastUltraSequencerClicked) {
                            Slot nextSlot = clickInOrderSlots[lastUltraSequencerClicked];
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, nextSlot.slotNumber, 0, 0, (EntityPlayer) mc.thePlayer);
                            until = lastUltraSequencerClicked + 1;
                            tickAmount = 0;
                        }
                        if (clickInOrderSlots[lastUltraSequencerClicked] != null && player.inventory.getItemStack() == null && tickAmount == 18 && lastUltraSequencerClicked < 1) {
                            Slot nextSlot = clickInOrderSlots[lastUltraSequencerClicked];
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, nextSlot.slotNumber, 0, 0, (EntityPlayer) mc.thePlayer);
                            tickAmount = 0;
                            until = 1;
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        clickInOrderSlots = new Slot[36];
        lastChronomatronRound = 0;
        chronomatronPattern.clear();
        chronomatronMouseClicks = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        tickAmount++;
        if (tickAmount % 20 == 0)
            tickAmount = 0;
        if (mc.currentScreen instanceof GuiChest &&
                Settings.autoEnchant) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            List<Slot> invSlots = ((GuiChest) mc.currentScreen).inventorySlots.inventorySlots;
            String chestName = chest.getLowerChestInventory().  getDisplayName().getUnformattedText().trim();
            if (chestName.startsWith("Ultrasequencer (") && ((Slot) invSlots.get(49)).getStack() != null && ((Slot) invSlots.get(49)).getStack().getDisplayName().equals("§aRemember the pattern!"))
                for (int l = 9; l <= 44; l++) {
                    if (invSlots.get(l) != null && (
                            (Slot) invSlots.get(l)).getStack() != null) {
                        String itemName = StringUtils.stripControlCodes(((Slot) invSlots.get(l)).getStack().getDisplayName());
                        if (itemName.matches("\\d+")) {
                            int number = Integer.parseInt(itemName);
                            clickInOrderSlots[number - 1] = invSlots.get(l);
                        }
                    }
                }
        }
    }
}
