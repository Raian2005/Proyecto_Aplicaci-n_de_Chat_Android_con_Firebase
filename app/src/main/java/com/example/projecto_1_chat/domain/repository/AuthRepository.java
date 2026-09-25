package com.example.projecto_1_chat.domain.repository;

public interface AuthRepository {
    void login(String email, String password, AuthCallback callback);

    void register(String name, String email, String password, AuthCallback callback);

    boolean isUserLoggedIn();

    String getCurrentUserId();
}
