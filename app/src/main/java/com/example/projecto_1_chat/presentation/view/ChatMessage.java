package com.example.projecto_1_chat.presentation.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projecto_1_chat.R;
import com.example.projecto_1_chat.databinding.ActivityChatMessageBinding;
import com.example.projecto_1_chat.databinding.ActivityCreatechatBinding;
import com.example.projecto_1_chat.presentation.adapter.ChatMessageAdapter;
import com.example.projecto_1_chat.presentation.utils.ImageBase64;
import com.example.projecto_1_chat.presentation.viewmodel.ChatMessageViewModel;
import com.example.projecto_1_chat.presentation.viewmodel.LoginViewModel;

public class ChatMessage extends AppCompatActivity {

    ActivityChatMessageBinding binding;

    LoginViewModel loginViewModel;
    ChatMessageViewModel chatMessageViewModel;
    ChatMessageAdapter chatMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidx.activity.EdgeToEdge.enable(this);

        binding = ActivityChatMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String chatId = getIntent().getStringExtra("chatId");
        String otherUserName = getIntent().getStringExtra("otherUserName");

        if(otherUserName != null){
            binding.toolbarChatMessage.setTitle(otherUserName);
        }

        loginViewModel = new LoginViewModel();
        chatMessageViewModel = new ChatMessageViewModel();

        String currentUserId = loginViewModel.getCurrentUserId();
        chatMessageAdapter = new ChatMessageAdapter(currentUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.recyclerViewMessage.setLayoutManager(layoutManager);
        binding.recyclerViewMessage.setAdapter(chatMessageAdapter);

        chatMessageViewModel.getChatMessege().observe(this, messages -> {
            chatMessageAdapter.setMessages(messages);
            if(messages != null && !messages.isEmpty()){
                binding.recyclerViewMessage.scrollToPosition(messages.size() - 1);
            }
        });

        chatMessageViewModel.listenToMessages(chatId);

        binding.buttonSendMessage.setOnClickListener(v->{
            String text = binding.editTextMessageSendInput.getText().toString().trim();
            if(!text.isEmpty()){
                chatMessageViewModel.sendMessage(chatId, currentUserId, text);
                binding.editTextMessageSendInput.setText("");
            }
        });

        binding.buttonSelectImage.setOnClickListener(v -> {
            selectImageGallery.launch("image/*");
        });

    }

    private final ActivityResultLauncher<String> selectImageGallery =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if(uri != null){
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        String base64Image = ImageBase64.bitmapToBase64(bitmap);

                        String chatId = getIntent().getStringExtra("chatId");
                        String currentUserId = loginViewModel.getCurrentUserId();

                        chatMessageViewModel.sendImageMessage(chatId, currentUserId, base64Image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
}