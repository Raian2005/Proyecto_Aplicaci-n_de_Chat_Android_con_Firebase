package com.example.projecto_1_chat.data.repository;

import com.example.projecto_1_chat.domain.model.Message;
import com.example.projecto_1_chat.domain.repository.MessageCallback;
import com.example.projecto_1_chat.domain.repository.MessageRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.List;

public class MessageRepositoryImpl implements MessageRepository {

    private FirebaseFirestore mFirestore;
    private ListenerRegistration messageListener;

    public MessageRepositoryImpl(){
        this.mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void listenToMessage(String chatId, MessageCallback callback) {
        messageListener = mFirestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if(error != null ){
                        callback.onError(error.getMessage());
                        return;
                    }
                    if(value != null){
                        List<Message> messages =  value.toObjects(Message.class);
                        callback.onSuccess(messages);
                    }
                });
    }

    @Override
    public void sendMessage(String chatId, String senderId, String text, MessageCallback callback) {
        DocumentReference docRef = mFirestore.
                collection("chats")
                .document(chatId)
                .collection("messages").document();
        String messageId = docRef.getId();
        Timestamp datetime = Timestamp.now();
        Message messageObjet = new Message(messageId, senderId, text, datetime);

        docRef.set(messageObjet).addOnSuccessListener(aVoid -> {
            mFirestore.collection("chats").document(chatId)
                    .update("lastMensaje", text,
                    "lastMensajeSenderId", senderId,
                    "lastMensajeHora", FieldValue.serverTimestamp());
            callback.onSuccess(null);
        }).addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    @Override
    public void removeMessagesLister() {
        if(messageListener != null){
            messageListener.remove();
            messageListener = null;
        }
    }
}
