package com.example.projecto_1_chat.presentation.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecto_1_chat.data.repository.AuthRepositoryImpl;
import com.example.projecto_1_chat.databinding.MessageReceivedBinding;
import com.example.projecto_1_chat.databinding.MessageSendBinding;
import com.example.projecto_1_chat.domain.model.Message;
import com.example.projecto_1_chat.domain.repository.AuthRepository;
import com.example.projecto_1_chat.presentation.utils.ImageBase64;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Message> messages = new ArrayList<Message>();
    private final String currentUserId;

    public ChatMessageAdapter(String currentUserId){
        this.currentUserId = currentUserId;
    }

    static int TYPE_SEND = 1;
    static int TYPE_RECEIVED = 2;

    private String formatTime(Timestamp timestamp){
        if(timestamp == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", java.util.Locale.getDefault());
        return dateFormat.format(timestamp.toDate());
    }

    public void setMessages(List<Message> newMessages){
        messages.clear();
        if(newMessages != null){
            messages.addAll(newMessages);
        }

        notifyDataSetChanged();
    }

    public static class SendViewHolder extends RecyclerView.ViewHolder {
        public final MessageSendBinding binding;
        public SendViewHolder(MessageSendBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        public final MessageReceivedBinding binding;

        public ReceivedViewHolder(MessageReceivedBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public int getItemViewType(int position){
        Message message = messages.get(position);
        if(message.getUserChatId() != null && message.getUserChatId().equals(currentUserId)){
            return TYPE_SEND;
        } else {
            return TYPE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType == TYPE_SEND){
            MessageSendBinding binding = MessageSendBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new SendViewHolder(binding);
        } else {
            MessageReceivedBinding binding = MessageReceivedBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ReceivedViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        Bitmap imageBitmap = ImageBase64.base64ToBitmap(message.getImageBase64());

        if (holder instanceof SendViewHolder) {
            SendViewHolder sendHolder = (SendViewHolder) holder;

            if (imageBitmap != null) {
                sendHolder.binding.imageMessageSend.setVisibility(ViewGroup.VISIBLE);
                sendHolder.binding.imageMessageSend.setImageBitmap(imageBitmap);

                if (message.getText() == null || message.getText().trim().isEmpty()) {
                    sendHolder.binding.textMessageSend.setVisibility(ViewGroup.GONE);
                } else {
                    sendHolder.binding.textMessageSend.setVisibility(ViewGroup.VISIBLE);
                }
            } else {
                sendHolder.binding.imageMessageSend.setVisibility(ViewGroup.GONE);
                sendHolder.binding.textMessageSend.setVisibility(ViewGroup.VISIBLE);
            }

            sendHolder.binding.textMessageSend.setText(message.getText());
            sendHolder.binding.textDateMessageSend.setText(formatTime(message.getTimestamp()));

        } else if (holder instanceof ReceivedViewHolder) {
            ReceivedViewHolder receivedHolder = (ReceivedViewHolder) holder;

            if (imageBitmap != null) {
                receivedHolder.binding.imageMessageReceived.setVisibility(ViewGroup.VISIBLE);
                receivedHolder.binding.imageMessageReceived.setImageBitmap(imageBitmap);

                if (message.getText() == null || message.getText().trim().isEmpty()) {
                    receivedHolder.binding.textMessageReceived.setVisibility(ViewGroup.GONE);
                } else {
                    receivedHolder.binding.textMessageReceived.setVisibility(ViewGroup.VISIBLE);
                }
            } else {
                receivedHolder.binding.imageMessageReceived.setVisibility(ViewGroup.GONE);
                receivedHolder.binding.textMessageReceived.setVisibility(ViewGroup.VISIBLE);
            }

            receivedHolder.binding.textMessageReceived.setText(message.getText());
            receivedHolder.binding.textDateMessageReceived.setText(formatTime(message.getTimestamp()));
        }
    }
}
