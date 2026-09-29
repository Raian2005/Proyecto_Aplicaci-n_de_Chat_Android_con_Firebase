package com.example.projecto_1_chat.data.repository;

import com.example.projecto_1_chat.domain.model.Chat;
import com.example.projecto_1_chat.domain.model.User;
import com.example.projecto_1_chat.domain.repository.ChatCallback;
import com.example.projecto_1_chat.domain.repository.ChatActionCallback;
import com.example.projecto_1_chat.domain.repository.ChatRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatRepositoryImpl implements ChatRepository {

    private final FirebaseFirestore firestore;
    private ListenerRegistration chatsListener;

    public ChatRepositoryImpl() {
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void createChatByEmail(
            String currentUserId,
            String email,
            ChatActionCallback callback
    ) {
        if (currentUserId == null || currentUserId.trim().isEmpty()) {
            callback.onError("No hay un usuario autenticado");
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            callback.onError("Debes escribir un Gmail");
            return;
        }

        firestore.collection("users")
                .whereEqualTo("email", email.trim())
                .limit(1)
                .get()
                .addOnSuccessListener(result -> {
                    if (result.isEmpty()) {
                        callback.onError("No existe un usuario con ese Gmail");
                        return;
                    }

                    DocumentSnapshot userDocument = result.getDocuments().get(0);
                    String otherUserId = userDocument.getId();

                    if (currentUserId.equals(otherUserId)) {
                        callback.onError("No puedes crear un chat contigo mismo");
                        return;
                    }

                    String chatId = createChatId(currentUserId, otherUserId);
                    firestore.collection("chats")
                            .document(chatId)
                            .get()
                            .addOnSuccessListener(chatDocument -> {
                                if (chatDocument.exists()) {
                                    Chat chat = chatDocument.toObject(Chat.class);
                                    if (chat != null) {
                                        chat.setId(chatDocument.getId());
                                    }
                                    callback.onSuccess(chat);
                                    return;
                                }

                                Map<String, Object> chatData = new HashMap<>();
                                chatData.put(
                                        "usersGroupId",
                                        Arrays.asList(currentUserId, otherUserId)
                                );
                                chatData.put("lastMensaje", "");
                                chatData.put("lastMensajeSenderId", "");
                                chatData.put("lastMensajeHora", null);
                                chatData.put("createdAt", FieldValue.serverTimestamp());

                                firestore.collection("chats")
                                        .document(chatId)
                                        .set(chatData)
                                        .addOnSuccessListener(unused -> {
                                            Chat chat = new Chat();
                                            chat.setId(chatId);
                                            chat.setusersGroupId(
                                                    Arrays.asList(currentUserId, otherUserId)
                                            );
                                            callback.onSuccess(chat);
                                        })
                                        .addOnFailureListener(error ->
                                                callback.onError(error.getMessage())
                                        );
                            })
                            .addOnFailureListener(error ->
                                    callback.onError(error.getMessage())
                            );
                })
                .addOnFailureListener(error -> callback.onError(error.getMessage()));
    }

    private String createChatId(String firstUserId, String secondUserId) {
        if (firstUserId.compareTo(secondUserId) < 0) {
            return firstUserId + "_" + secondUserId;
        }
        return secondUserId + "_" + firstUserId;
    }

    @Override
    public void listenToChats(String currentUserId, ChatCallback callback) {
        if (currentUserId == null || currentUserId.trim().isEmpty()) {
            callback.onError("No hay un usuario autenticado");
            return;
        }

        removeChatsListener();

        chatsListener = firestore.collection("chats")
                .whereArrayContains("usersGroupId", currentUserId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        callback.onError(error.getMessage());
                        return;
                    }

                    if (snapshot == null) {
                        callback.onError("No se pudieron cargar los chats");
                        return;
                    }

                    loadOtherUsers(snapshot, currentUserId, callback);
                });
    }

    private void loadOtherUsers(
            QuerySnapshot snapshot,
            String currentUserId,
            ChatCallback callback
    ) {
        List<Chat> chats = new ArrayList<>();
        List<Chat> chatsWithUser = new ArrayList<>();

        for (DocumentSnapshot document : snapshot.getDocuments()) {
            Chat chat = document.toObject(Chat.class);
            if (chat != null) {
                chat.setId(document.getId());
                chats.add(chat);
            }
        }

        if (chats.isEmpty()) {
            callback.onSuccess(chats);
            return;
        }

        AtomicInteger pendingUsers = new AtomicInteger(chats.size());

        for (Chat chat : chats) {
            String otherUserId = getOtherUserId(chat, currentUserId);

            if (otherUserId == null) {
                if (pendingUsers.decrementAndGet() == 0) {
                    callback.onSuccess(chatsWithUser);
                }
                continue;
            }

            firestore.collection("users")
                    .document(otherUserId)
                    .get()
                    .addOnSuccessListener(userDocument -> {
                        User user = userDocument.toObject(User.class);
                        if (user != null) {
                            chat.setOtherUserName(user.getName());
                            chat.setOtherUserProfileImageUrl(user.getProfileImageUrl());
                        }
                        chatsWithUser.add(chat);

                        if (pendingUsers.decrementAndGet() == 0) {
                            callback.onSuccess(chatsWithUser);
                        }
                    })
                    .addOnFailureListener(error ->
                            callback.onError(error.getMessage())
                    );
        }
    }

    private String getOtherUserId(Chat chat, String currentUserId) {
        if (chat.getusersGroupId() == null) {
            return null;
        }

        for (String userId : chat.getusersGroupId()) {
            if (userId != null && !userId.equals(currentUserId)) {
                return userId;
            }
        }

        return null;
    }

    @Override
    public void removeChatsListener() {
        if (chatsListener != null) {
            chatsListener.remove();
            chatsListener = null;
        }
    }
}
