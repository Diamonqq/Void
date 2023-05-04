package com.conutik.classes;

import com.conutik.features.AutoOpen;
import com.conutik.helpers.Helpers;
import net.minecraft.client.Minecraft;

public class QueueItem {
    private final String command;

    public QueueItem(String command) {
        this.command = command;
    }

    public void openAuction() {
        AutoOpen.autoOpenWorking = true;
        Helpers.sendDebugMessage("Executing: " + this.command);
        (Minecraft.getMinecraft()).thePlayer.sendChatMessage(this.command);
    }

    private void click(int id, int index) {
        (Minecraft.getMinecraft()).playerController.windowClick(id, index, 0, 3, Minecraft.getMinecraft().thePlayer);
    }
}
