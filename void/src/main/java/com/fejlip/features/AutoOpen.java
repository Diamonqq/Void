package com.fejlip.features;

import com.fejlip.Macro;
import com.fejlip.helpers.Helpers;
import com.fejlip.helpers.QueueItem;
import net.minecraft.network.play.server.S2FPacketSetSlot;

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
        if (!Macro.getInstance().getConfig().isAutoOpenEnabled()) return;
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
}
