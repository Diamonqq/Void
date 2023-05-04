package com.conutik.helpers;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClassUtils {

    private static final ClassUtils INSTANCE = new ClassUtils();

    public static ClassUtils getInstance() {
        return INSTANCE;
    }

    public String currentlyOpenChestName = "";
    public String lastOpenChestName = "";

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onGuiOpen(GuiOpenEvent event) {
        if (!Utils.isInSkyblock) return;

        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            ContainerChest container = (ContainerChest) chest.inventorySlots;

            currentlyOpenChestName = container.getLowerChestInventory().getDisplayName().getUnformattedText();
            lastOpenChestName = currentlyOpenChestName;
        } else {
            currentlyOpenChestName = "";
        }
    }
}
