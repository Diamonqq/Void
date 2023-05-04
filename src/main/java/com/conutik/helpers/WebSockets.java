package com.conutik.helpers;

import com.conutik.classes.ChatMessageData;
import com.conutik.classes.FlipData;
import com.conutik.classes.JsonStringCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import static com.conutik.cofl.HandleRequests.*;

public class WebSockets extends WebSocketClient {
    public static Gson gson = new Gson();
    public WebSockets(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to websocket");
    }

    @Override
    public void onMessage(String message) {
        JsonStringCommand cmd = gson.fromJson(message, JsonStringCommand.class);
        System.out.println(cmd);
        switch (cmd.getType()) {
            case WriteToChat: {
                WriteToChat(cmd.GetAs(new TypeToken<ChatMessageData>() {
                }));
                break;
            }
            case ChatMessage: {
                ChatMessage(cmd.GetAs(new TypeToken<ChatMessageData[]>() {
                }));
                break;
            }
            case Flip: {
                Flip(cmd.GetAs(new TypeToken<FlipData>() {
                }));
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
