package com.example.projecto_1_chat.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projecto_1_chat.data.repository.ChatRepositoryImpl;
import com.example.projecto_1_chat.domain.model.Chat;
import com.example.projecto_1_chat.domain.repository.ChatCallback;
import com.example.projecto_1_chat.domain.repository.ChatActionCallback;
import com.example.projecto_1_chat.domain.repository.ChatRepository;

import java.util.List;

public class ChatListViewModel extends ViewModel {

    private final ChatRepository chatRepository;
    private final MutableLiveData<List<Chat>> chats = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    public ChatListViewModel() {
        chatRepository = new ChatRepositoryImpl();
    }

    public LiveData<List<Chat>> getChats() {
        return chats;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public void listenToChats(String currentUserId) {
        chatRepository.listenToChats(currentUserId, new ChatCallback() {
            @Override
            public void onSuccess(List<Chat> chatList) {
                chats.postValue(chatList);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    public void createChatByEmail(String currentUserId, String email) {
        chatRepository.createChatByEmail(currentUserId, email, new ChatActionCallback() {
            @Override
            public void onSuccess(Chat chat) {
                successMessage.postValue("Chat creado correctamente");
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    @Override
    protected void onCleared() {
        chatRepository.removeChatsListener();
        super.onCleared();
    }
}
