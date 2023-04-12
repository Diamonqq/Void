package com.conutik.config.commands;

import com.conutik.Macro;
import gg.essential.api.utils.GuiUtil;
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

        GuiUtil.open(Macro.settings.gui());
    }


}
