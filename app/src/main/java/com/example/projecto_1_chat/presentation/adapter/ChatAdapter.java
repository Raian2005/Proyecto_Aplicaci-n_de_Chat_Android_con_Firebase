package com.example.projecto_1_chat.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecto_1_chat.databinding.ItemChatBinding;
import com.example.projecto_1_chat.domain.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    private final List<Chat> chats = new ArrayList<>();
    private final String currentUserId;
    private final OnChatClickListener listener;

    public ChatAdapter(String currentUserId, OnChatClickListener listener) {
        this.currentUserId = currentUserId;
        this.listener = listener;
    }

    public void setChats(List<Chat> newChats) {
        chats.clear();
        if (newChats != null) {
            chats.addAll(newChats);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding binding = ItemChatBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        String otherUserName = chat.getOtherUserName();
        holder.binding.textViewNameChat.setText(
                otherUserName == null || otherUserName.trim().isEmpty()
                        ? "Usuario"
                        : otherUserName
        );

        String lastMessage = chat.getlastMensaje();
        holder.binding.textViewLastMessage.setText(
                lastMessage == null || lastMessage.trim().isEmpty()
                        ? ""
                        : lastMessage
        );

        holder.itemView.setOnClickListener(v -> listener.onChatClick(chat));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatBinding binding;

        public ChatViewHolder(ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
