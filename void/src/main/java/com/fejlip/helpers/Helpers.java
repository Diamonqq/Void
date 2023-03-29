package com.fejlip.helpers;

import com.fejlip.Macro;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public final class Helpers {

    public static String prefix = "§f§[§0§lVOID§f§] ";

    public static void sendDebugMessage(String message) {
        if (Macro.getInstance().getConfig().isDebug())
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(prefix + message));
    }

    public static void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(prefix + message));
    }
}
