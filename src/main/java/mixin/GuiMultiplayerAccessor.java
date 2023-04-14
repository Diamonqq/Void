package mixin;

import net.minecraft.client.gui.GuiMultiplayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiMultiplayer.class)
public interface GuiMultiplayerAccessor {
    @Invoker
    public void invokeConnectToSelected();
}

