package com.conutik.helpers;

import com.conutik.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;

public final class Helpers {

    public static String prefix;
//    public static String prefix = "§f[§8§lVOID§f] > ";
//    public static String prefix = "§f[§0§lVOID§f] > ";
//    prefix = !Settings.modPrefix.isEmpty() ? Settings.modPrefix.replaceAll("&", "§") : "§0§lVOID§r > ";

    public static void sendDebugMessage(String message) {
        if (Settings.debug)
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(getPrefix() + "§r " + message));
    }

    public static void sendChatMessageNoPrefix(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(getPrefix() + "§r " + message));
    }

    public static void sendChatMessageWithClick(String message, ClickEvent clickEvent) {
        ChatComponentText msg = new ChatComponentText(getPrefix() + "§r " + message);
        msg.setChatStyle(msg.getChatStyle().setChatClickEvent(clickEvent));
        Minecraft.getMinecraft().thePlayer.addChatMessage(msg);
    }

    public static void sendChatMessageWithClickAndHover(String message, ClickEvent clickEvent, HoverEvent hoverEvent) {
        ChatComponentText msg = new ChatComponentText(prefix + "§r " + message);
        msg.setChatStyle(msg.getChatStyle().setChatClickEvent(clickEvent));
        msg.setChatStyle(msg.getChatStyle().setChatHoverEvent(hoverEvent));
        Minecraft.getMinecraft().thePlayer.addChatMessage(msg);
    }

    public static String getPrefix() {
        return (!Settings.modPrefix.isEmpty() ? Settings.modPrefix.replaceAll("&", "§") + "§r" : "§0§lVOID§r >");
    }
}
