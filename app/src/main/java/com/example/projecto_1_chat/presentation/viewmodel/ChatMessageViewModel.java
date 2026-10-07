package com.example.projecto_1_chat.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projecto_1_chat.data.repository.MessageRepositoryImpl;
import com.example.projecto_1_chat.domain.model.Message;
import com.example.projecto_1_chat.domain.repository.MessageCallback;
import com.example.projecto_1_chat.domain.repository.MessageRepository;

import java.util.List;

public class ChatMessageViewModel extends ViewModel {
    private final MessageRepository messageRepository;

    private final MutableLiveData<List<Message>> AllMessages = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<List<Message>> getChatMessege(){
        return AllMessages;
    }

    public LiveData<String> getErrorChatMessage(){
        return errorMessage;
    }

    public ChatMessageViewModel(){
        this.messageRepository = new MessageRepositoryImpl();
    }

    public void listenToMessages(String chatId){
        messageRepository.listenToMessage(chatId, new MessageCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                AllMessages.setValue(messages);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public void sendMessage(String chatId, String senderId, String text){

        if(text == null || text.trim().isEmpty()){
            errorMessage.setValue("El mensaje debe contener algun texto");
            return;
        }

        messageRepository.sendMessage(chatId, senderId, text, new MessageCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public void sendImageMessage(String chatId, String senderId, String base64Image){
        if(base64Image == null || base64Image.trim().isEmpty()){
            errorMessage.setValue("El mensaje debe contener");
            return;
        }

        messageRepository.sendImageMessage(chatId, senderId, base64Image, new MessageCallback() {
            @Override
            public void onSuccess(List<Message> messages) {

            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    @Override
    protected void onCleared(){
        messageRepository.removeMessagesLister();
    }


}
