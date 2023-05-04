package com.conutik.cofl;

import com.conutik.classes.*;
import com.conutik.config.Settings;
import com.conutik.helpers.Variables;
import com.conutik.helpers.WebSockets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.stream.Stream;

public class HandleRequests {
    public static IChatComponent ChatMessage(Command<ChatMessageData[]> cmd) {
        ChatMessageData[] list = cmd.getData();

        IChatComponent master = new ChatComponentText("");
        String fullMessage = ChatMessageDataToString(list);

        for (ChatMessageData wcmd : list) {
            IChatComponent comp = CommandToChatComponent(wcmd, fullMessage);
            if (comp != null)
                master.appendSibling(comp);
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(master);
        return master;
    }

    public static String ChatMessageDataToString(ChatMessageData[] messages) {
        Stream<String> stream = Arrays.stream(messages).map(message -> message.Text);
        String s = String.join(",", stream.toArray(String[]::new));
        stream.close();
        return s;
    }

    private static IChatComponent CommandToChatComponent(ChatMessageData wcmd, String fullMessage) {
        if (wcmd.Text != null) {
            IChatComponent comp = new ChatComponentText(wcmd.Text);

            ChatStyle style;
            if (wcmd.OnClick != null) {
                if (wcmd.OnClick.startsWith("http")) {
                    style = new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, wcmd.OnClick));
                } else {
                    style = new ChatStyle()
                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ah"));
                }
                comp.setChatStyle(style);
            }

            if (wcmd.Hover != null && !wcmd.Hover.isEmpty()) {
                if (comp.getChatStyle() == null)
                    comp.setChatStyle(new ChatStyle());
                comp.getChatStyle().setChatHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(wcmd.Hover)));
            }
            return comp;
        }
        return null;
    }

    public static void Flip(Command<FlipData> cmd) {
        System.out.println(cmd);
        //handle chat message
        ChatMessageData[] messages = cmd.getData().Messages;
        System.out.println(Arrays.toString(messages));
        SoundData sound = cmd.getData().Sound;
        if (sound != null && sound.Name != null) {
            PlaySound(sound.Name, sound.Pitch);
        }
        Command<ChatMessageData[]> showCmd = new Command<ChatMessageData[]>(CommandType.ChatMessage, messages);
        ChatMessage(showCmd);
        // trigger the keyevent to execute the event handler
//        CoflSky.Events.onKeyEvent(null);
        if(Settings.autoOpen) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/viewauction " + cmd.getData().Id);
        }
//        String json = WebSockets.gson.toJson(new RawCommand("chat", WebSockets.gson.toJson("Testing")));
//        System.out.println("Sending: " + json);
//        Variables.getWebsocket().send(json);
    }

    private static void PlaySound(String soundName, float pitch) {
        SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();

        // random.explode
        PositionedSoundRecord psr = PositionedSoundRecord
                .create(new ResourceLocation(soundName), pitch);

        handler.playSound(psr);
    }

    public static void WriteToChat(Command<ChatMessageData> cmd) {
        ChatMessageData wcmd = cmd.getData();

        IChatComponent comp = CommandToChatComponent(wcmd, wcmd.Text);
        if (comp != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(comp);
        }

    }
}
