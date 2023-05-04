package com.conutik.classes;

import com.conutik.cofl.HandleRequests;
import com.conutik.helpers.WebSockets;
import com.google.gson.annotations.SerializedName;

public class FlipData {

    @SerializedName("messages")
    public ChatMessageData[] Messages;
    @SerializedName("id")
    public String Id;
    @SerializedName("worth")
    public int Worth;
    @SerializedName("sound")
    public SoundData Sound;
    @SerializedName("render")
    public String Render;

    public FlipData() {
    }

    public FlipData(ChatMessageData[] messages, String id, int worth, SoundData sound, String render) {
        super();
        Messages = messages;
        Id = id;
        Worth = worth;
        Sound = sound;
        Render = render;
    }

    public String getMessageAsString(){
        return HandleRequests.ChatMessageDataToString(this.Messages);
    }
}
