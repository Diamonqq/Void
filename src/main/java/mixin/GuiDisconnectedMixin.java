package mixin;

import com.conutik.Macro;
import com.conutik.config.Settings;
import com.conutik.helpers.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GuiDisconnected.class)
public class GuiDisconnectedMixin extends GuiScreen {

    @Shadow
    private int field_175353_i;
    private GuiButton btnReconnect;

    @Inject(method = "initGui", at = @At("TAIL"))
    public void initGui(CallbackInfo ci) throws InterruptedException {
        int otherButtonY = this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(btnReconnect = new GuiButton(1, this.width / 2 - 100, otherButtonY + 24, "Reconnect"));

        btnReconnect.enabled = Macro.lastServer() != null;
        Thread.sleep(4000);
        if(Settings.autoConnect) this.actionPerformed(btnReconnect, null);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 1 && button.enabled) {
            Minecraft client = FMLClientHandler.instance().getClient();
            ServerData data = Macro.lastServer();
            Object gui = new GuiConnecting(this, client, data);
            FMLClientHandler.instance().showGuiScreen(gui);
        }
    }

    @Override
    public void updateScreen() {
        btnReconnect.enabled = Macro.lastServer() != null;
    }

}
