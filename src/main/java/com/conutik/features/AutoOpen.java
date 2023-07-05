package com.conutik.features;

import com.conutik.Macro;
import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import com.conutik.classes.PurchaseObject;
import com.conutik.classes.QueueItem;

import com.conutik.helpers.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.EnumChatFormatting;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.conutik.helpers.Utils.extractParameters;

public class AutoOpen {

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
        if (!Utils.isWorking) return;
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
        if (!Settings.buyWebhooks) return;
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
        if (te.isPresent())
        {
            HashMap<Integer, String> hash = te.get();
            Macro.getInstance().getPurchaseWaiter().add(hash.get(2).replaceAll("\\.$", "").replaceAll("\\!$", ""), new PurchaseObject(hash.get(2).replaceAll("\\.$", "").replaceAll("\\!$", ""), hash.get(4), hash.get(5), hash.get(7)));
        }
    }
}
