package com.conutik.mixin;

import com.conutik.Macro;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MultiplayerGUIMixin extends GuiScreen {

    @Shadow
    private ServerSelectionList serverListSelector;
    @Shadow
    private GuiButton btnEditServer;
    @Shadow
    private GuiButton btnSelectServer;
    @Shadow
    private GuiButton btnDeleteServer;
    private GuiButton btnReconnect;

    // Here should be able to do @Overwrite but doesn't work on my end for some reason lol
    @Inject(method = "createButtons", at = @At("HEAD"), cancellable = true)
    public void createButtons(CallbackInfo ci) throws InterruptedException {
        int offset = 50;
        this.buttonList.add(btnSelectServer = new GuiButton(1, this.width / 2 - 154 - offset, this.height - 52, 100, 20, I18n.format("selectServer.select")));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50 - offset, this.height - 52, 100, 20, I18n.format("selectServer.direct")));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50 - offset, this.height - 52, 100, 20, I18n.format("selectServer.add")));
        this.buttonList.add(btnReconnect = new GuiButton(9, this.width / 2 + 8 + 150 - offset, this.height - 52, 100, 20, "Reconnect"));

        this.buttonList.add(btnEditServer = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit")));
        this.buttonList.add(btnDeleteServer = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete")));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh")));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel")));
        selectServer(serverListSelector.func_148193_k());
        btnReconnect.enabled = Macro.lastServer() != null;
        ci.cancel();
//        Doenst let you join any other server lol
//        Thread.sleep(4000);
//        if(Settings.autoConnect) this.actionPerformed(btnReconnect, null);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 9 && button.enabled) {
            Minecraft client = FMLClientHandler.instance().getClient();
            ServerData data = Macro.lastServer();
            Object gui = new GuiConnecting(this, client, data);
            FMLClientHandler.instance().showGuiScreen(gui);
        }
    }

    public void reconnect() {
        Minecraft client = FMLClientHandler.instance().getClient();
        ServerData data = Macro.lastServer();
        Object gui = new GuiConnecting(this, client, data);
        FMLClientHandler.instance().showGuiScreen(gui);
    }

    @Inject(method = "updateScreen", at = @At("TAIL"))
    public void updateScreen(CallbackInfo ci) {
        btnReconnect.enabled = Macro.lastServer() != null;
    }

    @Shadow
    public abstract void selectServer(int index);

}
