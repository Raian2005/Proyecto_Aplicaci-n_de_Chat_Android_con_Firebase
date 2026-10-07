package com.example.projecto_1_chat.data.repository;

import com.example.projecto_1_chat.data.service.NotifFirebaseCloudMensaggesV1Sender;
import com.example.projecto_1_chat.domain.model.Message;
import com.example.projecto_1_chat.domain.repository.MessageCallback;
import com.example.projecto_1_chat.domain.repository.MessageRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.sql.Time;
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
        Message messageObjet = new Message(messageId, senderId, text, "", datetime);

        docRef.set(messageObjet).addOnSuccessListener(aVoid -> {
            mFirestore.collection("chats").document(chatId)
                    .update("lastMensaje", text,
                    "lastMensajeSenderId", senderId,
                    "lastMensajeHora", FieldValue.serverTimestamp());

            notificacionAppCerradaFCMv1(chatId, senderId, text);


            callback.onSuccess(null);
        }).addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    @Override
    public void sendImageMessage(String chatId, String senderId, String base64Image, MessageCallback callback) {
        DocumentReference docRef = mFirestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .document();

        String messageId = docRef.getId();
        Timestamp datetime = Timestamp.now();

        Message messageObject = new Message(messageId, senderId, "" , base64Image, datetime);

        docRef.set(messageObject).addOnSuccessListener( aVoid ->{
            mFirestore.collection("chats").document(chatId)
                    .update("lastMensaje", "Imagen",
                            "lastMensajeSenderId", senderId,
                            "lastMensajeHora", FieldValue.serverTimestamp());

            notificacionAppCerradaFCMv1(chatId, senderId, "Te envió una Imagen");
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

    private void notificacionAppCerradaFCMv1(String chatId, String senderId, String textMessage) {
        mFirestore.collection("chats").document(chatId).get()
                .addOnSuccessListener(chatDocument -> {
                    if (chatDocument.exists()) {
                        List<String> userGroup = (List<String>) chatDocument.get("usersGroupId");

                        String otherUserId = null;
                        if (userGroup != null) {
                            for (String uid : userGroup) {
                                if (!uid.equals(senderId)) {
                                    otherUserId = uid;
                                    break;
                                }
                            }
                        }

                        final String idOtherUserChat = otherUserId;

                        if (idOtherUserChat != null) {
                            mFirestore.collection("users").document(senderId).get()
                                    .addOnSuccessListener(senderDoc -> {
                                        String senderName = (senderDoc.exists() && senderDoc.getString("name") != null)
                                                ? senderDoc.getString("name") : "Nuevo Mensaje";

                                        mFirestore.collection("users").document(idOtherUserChat).get()
                                                .addOnSuccessListener(receiverDoc -> {
                                                    if (receiverDoc.exists()) {
                                                        String targetToken = receiverDoc.getString("token");
                                                        if (targetToken == null) {
                                                            targetToken = receiverDoc.getString("Token");
                                                        }

                                                        if (targetToken != null && !targetToken.isEmpty()) {
                                                            android.content.Context context = com.google.firebase.FirebaseApp.getInstance().getApplicationContext();
                                                            NotifFirebaseCloudMensaggesV1Sender.sendNotification(
                                                                    context, targetToken, senderName, textMessage);
                                                        }
                                                    }
                                                });
                                    });
                        }
                    }
                });
    }
}
