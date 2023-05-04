package com.conutik.helpers;

import java.net.URI;
import java.net.URISyntaxException;

public class Variables {
    public static WebSockets getWebsocket() {
        return websocket;
    }

    public static void initWebsocket(String username) throws URISyntaxException {
        Variables.websocket = new WebSockets(new URI("wss://sky.coflnet.com/modsocket?player=" + username + "&version=1.5.2-Alpha&sId=xy"));
        Variables.websocket.connect();
    }

    public static WebSockets websocket;
}

