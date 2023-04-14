package com.conutik.helpers;

import com.conutik.classes.PurchaseObject;

import java.util.HashMap;

public class PurchaseWaiter {
    private final HashMap<String, PurchaseObject> map = new HashMap<>();

    public void add(String id, PurchaseObject obj) {
        this.map.put(id, obj);
        Helpers.sendDebugMessage("Added " + id);
        Utils.setTimeout(() -> {
            Helpers.sendDebugMessage(id + " removed from map");
            this.map.remove(id);
        }, 20000);
    }

    public PurchaseObject get(String itemName) {
        PurchaseObject item = this.map.get(itemName);
        this.map.remove(itemName);
        Helpers.sendDebugMessage("Gotten " + itemName);
        return item;
    }
}
