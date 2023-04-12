package com.conutik.helpers;

import com.conutik.classes.PurchaseObject;

import java.util.HashMap;

public class PurchaseWaiter {
    private final HashMap<String, PurchaseObject> map = new HashMap<>();

    public void add(String id, PurchaseObject obj) {
        Helpers.sendDebugMessage("Added item");
        this.map.put(id, obj);
        Utils.setTimeout(() -> {
            Helpers.sendDebugMessage("Item removed from map");
            this.map.remove(id);
        }, 10000);
    }

    public PurchaseObject get(String itemName) {
        Helpers.sendDebugMessage("Gotten item");
        PurchaseObject item = this.map.get(itemName);
        this.map.remove(itemName);
        return item;
    }
}
