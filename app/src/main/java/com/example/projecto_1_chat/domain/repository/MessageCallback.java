package com.example.projecto_1_chat.domain.repository;

import java.util.List;
import com.example.projecto_1_chat.domain.model.Message;

public interface MessageCallback{
    void onSuccess(List<Message> messages);
    void onError(String error);
}
