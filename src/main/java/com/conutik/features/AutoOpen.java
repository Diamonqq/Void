package com.conutik.features;

import com.conutik.Macro;
import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import com.conutik.classes.PurchaseObject;
import com.conutik.classes.QueueItem;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern zeroPattern = Pattern.compile("(?<=\\{0}\\z*)..");
    private static final Pattern twoPattern = Pattern.compile("(?<=\\{2}\\z*)..");
    private static final Pattern fourPattern = Pattern.compile("(?<=\\{4}\\z*)..");
    private static final Pattern fivePattern = Pattern.compile("(?<=\\{5}\\z*)..");
    private static final Pattern sevenPattern = Pattern.compile("(?<=\\{7}\\z*)..");

    public void handleWebhook(String str) {
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
        text = text.replaceAll("§.", "");
        String flipperMsg = Settings.msgFormat.replaceAll("§.", "");
        String startOfMessage = flipperMsg.split("\\{")[0];
        flipperMsg = flipperMsg.replaceAll("\\{1}", "");
        flipperMsg = flipperMsg.replaceAll("\\{3}", "");
        flipperMsg = flipperMsg.replaceAll("\\{6}", "");
        flipperMsg = flipperMsg.replaceAll("\\{8}", "");
        flipperMsg = flipperMsg.replaceAll("\\{9}", "");
        flipperMsg = flipperMsg.replaceAll("\\{10}", "");
        if (text.startsWith(startOfMessage)) {
            Matcher zeroMatch = zeroPattern.matcher(flipperMsg);
            Matcher twoMatch = twoPattern.matcher(flipperMsg);
            Matcher fourMatch = fourPattern.matcher(flipperMsg);
            Matcher fiveMatch = fivePattern.matcher(flipperMsg);
            Matcher sevenMatch = sevenPattern.matcher(flipperMsg);
            String zeroValue = "Cannot find value";
            String twoValue = "Cannot find value";
            String fourValue = "Cannot find value";
            String fiveValue = "Cannot find value";
            String sevenValue = "Cannot find value";
            if (zeroMatch.find())
                zeroValue = text.split(flipperMsg.split("(\\{0}).")[0].replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[1].split(zeroMatch.group(0).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[0];
            flipperMsg = flipperMsg.replaceAll("\\{0}", zeroValue);
            if (twoMatch.find())
                twoValue = text.split(flipperMsg.split("(\\{2}).")[0].replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[1].split(twoMatch.group(0).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[0];
            twoValue = twoValue.replace(".", "");
            flipperMsg = flipperMsg.replaceAll("\\{2}", twoValue);
            if (fourMatch.find())
                fourValue = text.split(flipperMsg.split("(\\{4}).")[0].replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[1].split(fourMatch.group(0).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[0];
            flipperMsg = flipperMsg.replaceAll("\\{4}", fourValue);
            if (fiveMatch.find())
                fiveValue = text.split(flipperMsg.split("(\\{5}).")[0].replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[1].split(fiveMatch.group(0).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[0];
            flipperMsg = flipperMsg.replaceAll("\\{5}", fiveValue);
            if (sevenMatch.find())
                sevenValue = text.split(flipperMsg.split("(\\{7}).")[0].replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[1].split(sevenMatch.group(0).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"))[0];
            flipperMsg = flipperMsg.replaceAll("\\{7}", sevenValue);
            Macro.getInstance().getPurchaseWaiter().add(twoValue, new PurchaseObject(twoValue, fourValue, fiveValue, sevenValue));
        }
    }
}
