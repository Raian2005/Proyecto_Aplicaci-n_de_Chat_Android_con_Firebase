package com.example.projecto_1_chat.domain.model;

import com.google.firebase.Timestamp;

public class Message {
    public String id;
    public String userChatId;
    public String text;
    public Timestamp timestamp;

    public Message(){}

    public Message(String id, String userChatId, String text, Timestamp timestamp){
        this.id = id;
        this.userChatId = userChatId;
        this.text = text;
        this.timestamp  = timestamp;
    }

    public String getUserChatId(){
        return  this.userChatId;
    }

    public String getId(){
        return this.id;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
    }

    public Timestamp getTimestamp(){
        return this.timestamp;
    }
}
