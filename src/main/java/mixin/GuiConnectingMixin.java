package mixin;

import com.conutik.Macro;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class GuiConnectingMixin {

    @Inject(method = "connect", at = @At("HEAD"))
    public void connect(String ip, int port, CallbackInfo ci) {
        ServerData data = new ServerData("LastServer", "mc.hypixel.net:25565", false);
        Macro.lastServer(data);
    }

}
