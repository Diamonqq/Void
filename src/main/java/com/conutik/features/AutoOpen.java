package com.conutik.features;

import com.conutik.Macro;
import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import com.conutik.classes.PurchaseObject;
import com.conutik.classes.QueueItem;

import com.conutik.helpers.Utils;
import com.conutik.helpers.Webhook;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.essential.universal.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.conutik.helpers.Utils.extractParameters;

public class AutoOpen {

    public static boolean autoOpenWorking = false;

    public AutoOpen() {
        System.setOut(new PrintStream(System.out) {
            public void println(String str) {
                AutoOpen.this.handleMessage(str);
                super.println(str);
            }
        });
    }


    public void handleMessage(String str) {
        handleWebhook(str);
        if (!Settings.autoOpen) return;
        if (!str.startsWith("Received:")) return;
        if(!Utils.isWorking) return;
        Pattern pattern = Pattern.compile("type[\\\":]*flip");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            pattern = Pattern.compile("/viewauction \\w+");
            matcher = pattern.matcher(str);
            if (matcher.find()) {
                String command = matcher.group();
                Helpers.sendDebugMessage("Adding to queue: " + command);
                Macro.getInstance().getQueue().add(new QueueItem(command));
                Macro.getInstance().getQueue().scheduleClear();
            }
        }
    }

    public void handleWebhook(String str) {
        handleCaptcha(str);
        if (!Settings.flipWebhooks) return;
        if (!str.startsWith("Received:")) return;
        Pattern pattern = Pattern.compile("type[\\\":]*flip");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) return;
        str = str.split("Received: ")[1];
        JsonParser parser = new JsonParser();
        JsonObject firstJson = (JsonObject) parser.parse(str);
        JsonObject secondJson = (JsonObject) parser.parse(firstJson.get("data").getAsString());
        String text = secondJson.get("messages").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();
        String flipperMsg = Settings.msgFormat.replaceAll("\\{1}", "").replaceAll("\\{3}", "").replaceAll("\\{2}", "{2+}").replaceAll("\\{6}", "{!}").replaceAll("\\{8}", "{!}").replaceAll("\\{9}", "{!}").replaceAll("\\{10}", "{!}");
        flipperMsg = EnumChatFormatting.getTextWithoutFormattingCodes(flipperMsg);
        text = EnumChatFormatting.getTextWithoutFormattingCodes(text);
        Optional<HashMap<Integer, String>> te = extractParameters(flipperMsg, text);
        if(te.isPresent())
        {
            HashMap<Integer, String> hash = te.get();
            Macro.getInstance().getPurchaseWaiter().add(hash.get(2).replaceAll("\\.$", "").replaceAll("\\!$", ""), new PurchaseObject(hash.get(2).replaceAll("\\.$", "").replaceAll("\\!$", ""), hash.get(4), hash.get(5), hash.get(7)));
        }
    }

    public void handleCaptcha(String str) {
        if(EnumChatFormatting.getTextWithoutFormattingCodes(str).contains("Hello there, you acted suspiciously like a macro bot")) {
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
                            .setColor(Settings.captchaColor)
                            .setDescription("```A WILD CAPTCHA HAS APPEARED. SOLVE IT TO CONTINUE YOUR FLIPPING JOURNEY !!!```\n" + description)
                            .setThumbnail("https://minotar.net/helm/" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "/600.png")
            );
            try{
                webhook.execute();
            } catch (IOException e) {
                Helpers.sendDebugMessage(e.getMessage());
            }
        }
    }
}
