package com.conutik.config.commands;

import com.conutik.Macro;
import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import com.conutik.helpers.Utils;
import gg.essential.api.utils.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;

import java.lang.reflect.Field;

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
        if (args.length == 0) {
            GuiUtil.open(Macro.settings.gui());
            return;
        }
        ;

        switch (args[0].toLowerCase()) {
            case "autobuy": {
                Settings.autoBuy = !Settings.autoBuy;
                Helpers.sendChatMessage("Autobuy " + (Settings.autoBuy ? "on" : "off"));
                break;
            }
            case "autoopen": {
                Settings.autoOpen = !Settings.autoOpen;
                Helpers.sendChatMessage("Auto Open " + (Settings.autoOpen ? "on" : "off"));
                break;
            }
            case "autoclaim": {
                Settings.autoClaim = !Settings.autoClaim;
                Helpers.sendChatMessage("Auto Claim " + (Settings.autoClaim ? "on" : "off"));
                break;
            }
            case "autoconnect": {
                Settings.autoConnect = !Settings.autoConnect;
                Helpers.sendChatMessage("Auto Connect " + (Settings.autoConnect ? "on" : "off"));
                break;
            }
            case "autosell": {
                Settings.autoSell = !Settings.autoSell;
                Helpers.sendChatMessage("Auto Sell " + (Settings.autoSell ? "on" : "off"));
                break;
            }
            case "test": {
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                PlayerControllerMP playerController = Minecraft.getMinecraft().playerController;
                Helpers.sendDebugMessage(String.valueOf(player.getPosition()));
                break;
//                player.sendChatMessage("/bz");
//                Utils.setTimeout(() -> {
//                    if(!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
//                        Helpers.sendChatMessage("You need a cookie to use auto enchanter!");
//                        return;
//                    }
//                    playerController.windowClick(player.openContainer.windowId, 36, 0, 3, player);
//                    Utils.setTimeout(() -> {
//                        playerController.windowClick(player.openContainer.windowId, 30, 0, 3, player);
//                        Utils.setTimeout(() -> {
//                            playerController.windowClick(player.openContainer.windowId, 14, 0, 3, player);
//                            Utils.setTimeout(() -> {
//                                playerController.windowClick(player.openContainer.windowId, 10, 0, 3, player);
//                                Utils.setTimeout(() -> {
//                                    playerController.windowClick(player.openContainer.windowId, 10, 0, 3, player);
//                                }, 500);
//                            }, 500);
//                        }, 500);
//                    }, 500);
//                }, 500);
            }
        }
    }


}
