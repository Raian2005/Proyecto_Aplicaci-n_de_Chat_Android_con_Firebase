package com.example.projecto_1_chat.domain.repository;

import com.example.projecto_1_chat.domain.model.Chat;

import java.util.List;

public interface ChatCallback {
    void onSuccess(List<Chat> chats);
    void onError(String error);
}
