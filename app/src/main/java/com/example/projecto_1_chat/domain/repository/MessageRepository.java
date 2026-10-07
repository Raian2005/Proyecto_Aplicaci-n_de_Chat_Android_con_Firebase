package com.example.projecto_1_chat.domain.repository;

public interface MessageRepository {
    void listenToMessage(String chatId, MessageCallback callback);
    void sendMessage(String chatId, String senderId, String text, MessageCallback callback);

    void sendImageMessage(String chatId, String senderId, String base64Image, MessageCallback callback);
    void removeMessagesLister();
}
