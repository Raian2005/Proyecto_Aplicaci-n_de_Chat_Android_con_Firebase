package com.example.projecto_1_chat.domain.repository;

import com.example.projecto_1_chat.domain.model.Chat;

public interface ChatActionCallback {
    void onSuccess(Chat chat);
    void onError(String error);
}
