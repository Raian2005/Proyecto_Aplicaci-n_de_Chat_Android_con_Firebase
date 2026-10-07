package com.example.projecto_1_chat.domain.repository;

public interface ChatRepository {
    void listenToChats(String currentUserId, ChatCallback callback);
    void createChatByEmail(String currentUserId, String email, ChatActionCallback callback);
    void removeChatsListener();
}
