package com.conutik.helpers;

import com.conutik.Macro;
import com.conutik.classes.PurchaseObject;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.text.NumberFormat;

public class FlipperMessage {
    public String getProfit() {
        return profit;
    }

    private String profit;
    private String item;
    private String boughtFor;
    private String sellingFor;
    NumberFormat myFormat = NumberFormat.getInstance();

    public void itemBought(String message) throws IOException {
        myFormat.setGroupingUsed(true);
        String item = message.split("You purchased ")[1].split(" for")[0];
        PurchaseObject itemObj = Macro.getInstance().getPurchaseWaiter().get(item);
        if(itemObj != null) {
            itemObj.bought();
        }
    }
}
