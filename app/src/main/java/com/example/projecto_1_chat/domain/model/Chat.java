package com.example.projecto_1_chat.domain.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Chat {
    private String id;
    private List<String> usersGroupId;
    private String lastMensaje;
    private String lastMensajeSenderId;
    private Timestamp lastMensajeHora;
    private Timestamp createdAt;
    private String otherUserName;
    private String otherUserProfileImageUrl;

    public Chat() {
    }

    public Chat(
            String id,
            List<String> usersGroupId,
            String lastMensaje,
            String lastMensajeSenderId,
            Timestamp lastMensajeHora,
            Timestamp createdAt
    ) {
        this.id = id;
        this.usersGroupId = usersGroupId;
        this.lastMensaje = lastMensaje;
        this.lastMensajeSenderId = lastMensajeSenderId;
        this.lastMensajeHora = lastMensajeHora;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getusersGroupId() {
        return usersGroupId;
    }

    public void setusersGroupId(List<String> usersGroupId) {
        this.usersGroupId = usersGroupId;
    }

    public String getlastMensaje() {
        return lastMensaje;
    }

    public void setlastMensaje(String lastMensaje) {
        this.lastMensaje = lastMensaje;
    }

    public String getlastMensajeSenderId() {
        return lastMensajeSenderId;
    }

    public void setlastMensajeSenderId(String lastMensajeSenderId) {
        this.lastMensajeSenderId = lastMensajeSenderId;
    }

    public Timestamp getlastMensajeHora() {
        return lastMensajeHora;
    }

    public void setlastMensajeHora(Timestamp lastMensajeHora) {
        this.lastMensajeHora = lastMensajeHora;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Exclude
    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    @Exclude
    public String getOtherUserProfileImageUrl() {
        return otherUserProfileImageUrl;
    }

    public void setOtherUserProfileImageUrl(String otherUserProfileImageUrl) {
        this.otherUserProfileImageUrl = otherUserProfileImageUrl;
    }
}