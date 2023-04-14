package com.conutik.config.commands;

import com.conutik.Macro;
import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import gg.essential.api.utils.GuiUtil;
import net.minecraft.client.Minecraft;
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
        if(args.length == 0) {
            GuiUtil.open(Macro.settings.gui());
            return;
        };

        switch(args[0].toLowerCase()) {
            case "autobuy": {
                Settings.autoBuy = !Settings.autoBuy;
                Helpers.sendChatMessage("Autobuy " + (Settings.autoBuy ? "off" : "on"));
                break;
            }
            case "autoopen": {
                Settings.autoOpen = !Settings.autoOpen;
                Helpers.sendChatMessage("Auto Open " + (Settings.autoOpen ? "off" : "on"));
                break;
            }
            case "autoclaim": {
                Settings.autoClaim = !Settings.autoClaim;
                Helpers.sendChatMessage("Auto Claim " + (Settings.autoClaim ? "off" : "on"));
                break;
            }
            case "autoconnect": {
                Settings.autoConnect = !Settings.autoConnect;
                Helpers.sendChatMessage("Auto Connect " + (Settings.autoConnect ? "off" : "on"));
                break;
            }
        }
    }


}
