package com.conutik;

import com.conutik.config.Settings;
import com.conutik.config.commands.MacroCommand;
import com.conutik.features.AutoBuy;
import com.conutik.features.AutoOpen;
import com.conutik.features.EnchantMacro;
import com.conutik.features.Packets;
import com.conutik.helpers.ClassUtils;
import com.conutik.helpers.PurchaseWaiter;
import com.conutik.helpers.Queue;
import com.conutik.helpers.VersionChecker;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "examplemod", version = "2.1.0")
public class Macro {
    public static final String version = "2.1.0";
    private static Macro instance;
    private final Queue queue = new Queue();

    private final PurchaseWaiter purchaseWaiter = new PurchaseWaiter();
    private static ServerData lastData = new ServerData("LastServer", "mc.hypixel.net:25565", false);


    public static Settings settings = new Settings();
    public static Macro getInstance() {
        return instance;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        settings.preload();
        ClientCommandHandler.instance.registerCommand(new MacroCommand());
        MinecraftForge.EVENT_BUS.register(new AutoBuy());
        MinecraftForge.EVENT_BUS.register(new Packets());
        MinecraftForge.EVENT_BUS.register(new VersionChecker());
        MinecraftForge.EVENT_BUS.register(new EnchantMacro());
        MinecraftForge.EVENT_BUS.register(ClassUtils.getInstance());
        new AutoOpen();
    }

    public Queue getQueue() {
        return this.queue;
    }

    public PurchaseWaiter getPurchaseWaiter() {
        return this.purchaseWaiter;
    }


    public static void lastServer(ServerData data) {
        lastData = data;
    }

    public static ServerData lastServer() {
        return lastData;
    }

}
