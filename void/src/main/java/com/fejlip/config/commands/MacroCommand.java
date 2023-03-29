package com.fejlip.config.commands;

import com.fejlip.Macro;
import com.fejlip.config.Config;
import com.fejlip.helpers.Helpers;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

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

    public void processCommand(ICommandSender sender, String[] args) {
        Config config = Macro.getInstance().getConfig();
        if (args.length == 0) {
            Helpers.sendChatMessage("No arguments!");
            return;
        }
        switch (args[0]) {
            case "Help":
            case "help":
                Helpers.sendChatMessage("Help:");
                Helpers.sendChatMessage("/void help - Shows this message");
                Helpers.sendChatMessage("/void debug - Toggles debug mode");
                Helpers.sendChatMessage("/void autobuy - Toggles autobuy");
                Helpers.sendChatMessage("/void autoopen - Toggles autoopen");
                Helpers.sendChatMessage("/void bed <delay> - Returns current bed click speed or sets the delay between bed clicks in ms");
                break;
            case "autoBuy":
            case "autobuy":
                boolean autoBuy = config.toggleAutoBuy();
                Helpers.sendChatMessage("Autobuy " + (autoBuy ? "on" : "off"));
                break;
            case "autoOpen":
            case "autoopen":
                boolean autoOpen = config.toggleAutoOpen();
                if (Macro.getInstance().getThread().isAlive()) {
                    Macro.getInstance().getQueue().clear();
                    Macro.getInstance().getQueue().setRunning(false);
                    Macro.getInstance().getThread().interrupt();
                } else {
                    Macro.getInstance().getThread().start();
                }
                Helpers.sendChatMessage("Autoopen " + (autoOpen ? "on" : "off"));
                break;
            case "bed":
            case "Bed":
                if (args.length == 1) {
                    Helpers.sendChatMessage("Current bed click delay: " + config.getBedClickDelay());
                    return;
                }
                try {
                    int bedDelay = Integer.parseInt(args[1]);
                    config.setBedClickDelay(bedDelay);
                    Helpers.sendChatMessage("New bed click delay: " + bedDelay);
                } catch (NumberFormatException e) {
                    Helpers.sendChatMessage("Invalid bed click speed!");
                }
                break;
            case "Debug":
            case "debug":
                boolean debug = config.toggleDebug();
                Helpers.sendChatMessage("Debug " + (debug ? "on" : "off"));
                break;
            default:
                Helpers.sendChatMessage("Invalid arguments!");
                break;
        }
    }


}
