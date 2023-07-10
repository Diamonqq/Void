package com.conutik.config.commands;

import com.conutik.Macro;
import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import com.conutik.helpers.Webhook;
import gg.essential.api.utils.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.*;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.util.Session;

import static com.conutik.helpers.Formatting.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MacroCommand extends CommandBase {

    public MacroCommand() {
    }

    public String getCommandName() {
        return "void";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/void <setting> <value>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public void processCommand(ICommandSender sender, String[] arguments) {
        ArrayList<String> args = new ArrayList(Arrays.asList(arguments));
        if (args.size() == 0) {
            GuiUtil.open(Macro.settings.gui());
            return;
        };

        switch(args.get(0).toLowerCase()) {
            case "help":
                Helpers.sendChatMessage("Help:\n/void help - Shows this message you dumb fuck.\n" +
                    "/void debug - Toggles debug mode\n" +
                    "/void autobuy|ab - Toggles auto-buy\n" +
                    "/void autoopen|ao - Toggles auto open\n" +
                    "/void autoclaim|claim|ac - Toggles auto-claim\n" +
                    "/void bed <delay> - Returns the current bed click speed or sets the delay between bed clicks in MS"
                );
                break;
            case "autobuy":
            case "ab":
                Settings.autoBuy = !Settings.autoBuy;
                Helpers.sendChatMessage("Auto-Buy " + (Settings.autoBuy ? fmt_a+"enabled" : fmt_c+"disabled")+fmt_r+".");
                break;
            case "autoopen":
            case "ao":
                Settings.autoOpen = !Settings.autoOpen;
                Helpers.sendChatMessage("Auto-Open " + (Settings.autoOpen ? fmt_a+"enabled" : fmt_c+"disabled")+fmt_r+".");
                break;
            case "autoclaim":
            case "claim":
            case "ac":
                Settings.autoClaim = !Settings.autoClaim;
                Helpers.sendChatMessage("Auto-Claim " + (Settings.autoClaim ? fmt_a+"enabled" : fmt_c+"disabled")+fmt_r+".");
                break;
            case "autoconnect":
                Settings.autoConnect = !Settings.autoConnect;
                Helpers.sendChatMessage("Auto-Reconnect " + (Settings.autoConnect ? fmt_a+"enabled" : fmt_c+"disabled")+fmt_r+".");
                break;
            case "autoisland":
            case "island":
            case "ai":
                Settings.autoIsland = !Settings.autoIsland;
                Helpers.sendChatMessage("Auto-Island " + (Settings.autoIsland ? fmt_a+"enabled" : fmt_c+"disabled")+fmt_r+".");
                break;
            case "beddelay":
            case "bed":
                if (args.size() == 1) {
                    Helpers.sendChatMessage("Current bed click delay: " + Settings.bedDelay);
                    return;
                }
                try {
                    int newBedDelay = Integer.parseInt(args.get(1));
                    Settings.bedDelay = newBedDelay;
                    Helpers.sendChatMessage("New bed click delay: " + newBedDelay);
                } catch (NumberFormatException e) {
                    Helpers.sendChatMessage("Invalid bed click speed!");
                }
                break;
            case "bedthreaddelay":
            case "bedthread":
                if (args.size() == 1) {
                    Helpers.sendChatMessage("Current bed thread kill delay: " + Settings.bedThreadKillDelay);
                    return;
                }
                try {
                    int newBedThreadKillDelay = Integer.parseInt(args.get(1));
                    Settings.bedThreadKillDelay = newBedThreadKillDelay;
                    Helpers.sendChatMessage("New bed thread interrupt delay: " + newBedThreadKillDelay);
                } catch (NumberFormatException e) {
                    Helpers.sendChatMessage("Invalid bed thread interrupt speed!");
                }
                break;
            case "skip":
                Settings.fullSkip = !Settings.fullSkip;
                Helpers.sendChatMessage("Full Skip " + (Settings.fullSkip ? fmt_a+"enabled" : fmt_c+"disabled")+fmt_r+".");
                break;
            case "fullskipdelay":
            case "fullskip":
                if (args.size() == 1) {
                    Helpers.sendChatMessage("Current full skip delay: " + Settings.fullSkipDelay);
                    return;
                }
                try {
                    int newFullSkipDelay = Integer.parseInt(args.get(1));
                    Settings.fullSkipDelay = newFullSkipDelay;
                    Helpers.sendChatMessage("New full skip delay: " + newFullSkipDelay);
                } catch (NumberFormatException e) {
                    Helpers.sendChatMessage("Invalid full skip delay!");
                }
                break;
            case "prefix":
                if (args.size() == 1) {
                    Helpers.sendChatMessage("Options:\n§b/void prefix clear§r - Sets the prefix to the default value\n§b/void prefix <prefix>§r - Sets a custom prefix. Formatting is supported.\n§bExample§r: \"[&c&lVOID&r] >\"");
                } else if (args.size() == 2 && Objects.equals(args.get(1), "clear")) {
                    Settings.modPrefix = "&0&lVOID&r >";
                    Helpers.sendChatMessage("Custom mod prefix cleared. Using default prefix.");
                } else {
                    args.remove(0);
                    StringBuilder argsString = new StringBuilder();
                    for (int i = 0; i < args.size(); i++)
                        argsString.append(args.get(i)).append(i == args.size() - 1 ? "" : " ");
                    Helpers.sendChatMessageNoPrefix(Helpers.getPrefix() + " Message with old prefix.\n" + String.valueOf(argsString).replaceAll("&", "§") + "§r Message with new prefix.");
                    Settings.modPrefix = String.valueOf(argsString);
                }
                break;
            case "debug":
                Settings.debug = !Settings.debug;
                Helpers.sendChatMessage("Debug " + (Settings.debug ? fmt_a+"enabled" : fmt_c+"disabled")+fmt_r+".");
                break;
            default:
                GuiUtil.open(Macro.settings.gui());
        }
    }


}
