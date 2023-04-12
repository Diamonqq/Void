package com.conutik.classes;

import com.conutik.config.Settings;
import com.conutik.helpers.Helpers;
import com.conutik.helpers.Utils;
import com.conutik.helpers.Webhook;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;

public class PurchaseObject {
    private final String itemName;
    private final String itemSoldFor;
    private final String itemSellingFor;
    private final String itemProfitPercentage;
    private ItemStack itemStack;
    NumberFormat myFormat = NumberFormat.getInstance();

    public PurchaseObject(String itemName, String itemSoldFor, String itemSellingFor, String itemProfitPercent) {
        this.itemName = itemName;
        this.itemSoldFor = itemSoldFor;
        this.itemSellingFor = itemSellingFor;
        this.itemProfitPercentage = itemProfitPercent;
    }

    public void addStack(ItemStack stack) {
        itemStack = stack;
    }

    public void bought() {
        String content = "<@" + Settings.discordID + ">";
        String description = null;
        if(!Settings.flipWebhookDescription.isEmpty()) description = Settings.flipWebhookDescription;
        if(Settings.discordID.isEmpty()) content = "New flip found!";
        if(Settings.flipWebhookURL.isEmpty()) return;
        long purse = Utils.purse;
        long longValue = (long)Math.round(Double.parseDouble(this.itemSoldFor.toUpperCase().split("[KM]")[0]) * Long.parseLong(this.itemSoldFor.split("(?![0.0-9.0])")[1].toUpperCase().replaceAll("K", "1000").replaceAll("M", "1000000")));
        purse = (purse - longValue);
        Webhook webhook = new Webhook(Settings.flipWebhookURL);
        webhook.setContent(content);
        webhook.setAvatarUrl("https://cdn.discordapp.com/icons/1074429357642227853/f2f3c75446e8774afe5c448d83401153.png");
        webhook.setUsername("Void Flipper");
        webhook.addEmbed(
                new Webhook.EmbedObject()
                        .setTitle("Just purchased an item!")
                        .setColor(Color.BLACK)
                        .addField("Item:", this.itemName, false)
                        .addField("Selling for:", this.itemSoldFor, false)
                        .addField("Projected Price:", this.itemSellingFor, false)
                        .addField("Percentage profit:", this.itemProfitPercentage + "%", false)
                        .setDescription(description)
                        .setFooter("Purse: " + myFormat.format(purse), "")
                        .setThumbnail("https://minotar.net/helm/" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "/600.png")
        );
        try{
            webhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
            Helpers.sendDebugMessage(e.getMessage());
        }
    }
}


