package com.example.projecto_1_chat.domain.repository;
import com.example.projecto_1_chat.domain.model.User;

public interface AuthCallback {
    void onSuccess(User user);
    void onError(String errorMessage);
}
