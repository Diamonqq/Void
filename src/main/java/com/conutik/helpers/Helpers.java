package com.conutik.helpers;

import com.conutik.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;

public final class Helpers {

    public static String prefix = "§f§[§0§lVOID§f§] ";

    public static void sendDebugMessage(String message) {
        if (Settings.debug)
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(prefix + message));
    }

    public static void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(prefix + message));
    }

    public static void sendChatMessageWithClick(String message, ClickEvent clickEvent) {
        ChatComponentText msg = new ChatComponentText(prefix + message);
        msg.setChatStyle(msg.getChatStyle().setChatClickEvent(clickEvent));
        Minecraft.getMinecraft().thePlayer.addChatMessage(msg);
    }
}
