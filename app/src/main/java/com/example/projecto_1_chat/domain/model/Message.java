package com.example.projecto_1_chat.domain.model;

import com.google.firebase.Timestamp;

public class Message {
    private String id;
    private String userChatId;
    private String text;
    private Timestamp timestamp;

    private String imageBase64;

    public Message(){}

    public Message(String id, String userChatId, String text, String imageBase64, Timestamp timestamp){
        this.id = id;
        this.userChatId = userChatId;
        this.text = text;
        this.timestamp  = timestamp;
        this.imageBase64 = imageBase64;
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

    public String getImageBase64(){
        return this.imageBase64;
    }

    public void setImageBase64(String imageURL){
        this.imageBase64 = imageURL;
    }

    public Timestamp getTimestamp(){
        return this.timestamp;
    }
}
