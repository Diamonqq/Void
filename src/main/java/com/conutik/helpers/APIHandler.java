package com.conutik.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APIHandler {

    // Taken from DSM
    public static JsonObject getResponse(String urlString) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String input;
                StringBuilder response = new StringBuilder();

                while ((input = in.readLine()) != null) {
                    response.append(input);
                }
                in.close();

                Gson gson = new Gson();

                return gson.fromJson(response.toString(), JsonObject.class);
            } else {
                if (urlString.startsWith("https://api.hypixel.net/")) {
                    InputStream errorStream = conn.getErrorStream();
                    try (Scanner scanner = new Scanner(errorStream)) {
                        scanner.useDelimiter("\\Z");
                        String error = scanner.next();

                        Gson gson = new Gson();
                        return gson.fromJson(error, JsonObject.class);
                    }
                } else if (urlString.startsWith("https://api.mojang.com/users/profiles/minecraft/") && conn.getResponseCode() == 204) {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Failed with reason: Player does not exist."));
                } else {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Request failed. HTTP Error Code: " + conn.getResponseCode()));
                }
            }
        } catch (IOException ex) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "An error has occured. See logs for more details."));
            ex.printStackTrace();
        }

        return new JsonObject();
    }
}
