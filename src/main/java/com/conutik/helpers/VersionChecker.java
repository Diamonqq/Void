package com.conutik.helpers;


import com.conutik.Macro;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

public class VersionChecker {
    static boolean updateChecked = false;
    @SubscribeEvent
    public void updateCheck(PlayerEvent.PlayerLoggedInEvent event) {
        if (!updateChecked) {
            new Thread(() -> {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;

                System.out.println("Checking for updates...");
                JsonObject latestRelease = APIHandler.getResponse("https://api.github.com/repos/Diamonqq/Void/releases/latest");
                if(latestRelease.get("message").getAsString().equals("Not Found")) return;

                String latestTag = latestRelease.get("tag_name").getAsString();
                DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(Macro.version);
                DefaultArtifactVersion latestVersion = new DefaultArtifactVersion(latestTag.substring(1));

                if (currentVersion.compareTo(latestVersion) < 0) {
                    String releaseURL = latestRelease.get("html_url").getAsString();

                    Helpers.sendChatMessage("======================");
                    Helpers.sendChatMessage("  [UPDATE]  ");
                    Helpers.sendChatMessageWithClick("An new update is available for Void Flipper (CLICK ME)", new ClickEvent(ClickEvent.Action.OPEN_URL, releaseURL));
                    Helpers.sendChatMessage("======================");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    };
                }
            }).start();
        }
    }
}
