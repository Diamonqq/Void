package com.fejlip.helpers;

import net.minecraft.client.Minecraft;

public class QueueItem {
    private final String command;

    public QueueItem(String command) {
        this.command = command;
    }

    public void openAuction() {
        Helpers.sendDebugMessage("Executing: " + this.command);
        (Minecraft.getMinecraft()).thePlayer.sendChatMessage(this.command);
    }
}
