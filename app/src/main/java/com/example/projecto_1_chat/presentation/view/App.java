package com.example.projecto_1_chat.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projecto_1_chat.databinding.ActivityAppBinding;
import com.example.projecto_1_chat.presentation.adapter.ChatAdapter;
import com.example.projecto_1_chat.presentation.viewmodel.ChatListViewModel;
import com.example.projecto_1_chat.presentation.viewmodel.LoginViewModel;

public class App extends AppCompatActivity {

    ActivityAppBinding binding;

    LoginViewModel loginViewModel;
    ChatListViewModel chatListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new LoginViewModel();
        chatListViewModel = new ChatListViewModel();

        androidx.activity.EdgeToEdge.enable(this);

        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        ChatAdapter chatAdapter = new ChatAdapter(
                loginViewModel.getCurrentUserId(),
                chat -> {
                    Intent intent = new Intent(App.this, ChatMessage.class);
                    intent.putExtra("chatId", chat.getId());
                    intent.putExtra("otherUserName", chat.getOtherUserName());
                    startActivity(intent);
                }
        );
        binding.recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewChats.setAdapter(chatAdapter);

        chatListViewModel.getChats().observe(this, chatAdapter::setChats);
        chatListViewModel.listenToChats(loginViewModel.getCurrentUserId());

        chatListViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(App.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        chatListViewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(App.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        binding.agregarChat.setOnClickListener(v -> {
            Intent intent = new Intent(App.this, CreateChat.class);
            startActivity(intent);
        });

        binding.cerrarSesion.setOnClickListener(v -> {
            loginViewModel.logout();
            Intent intent = new Intent(App.this, Login.class);
            startActivity(intent);
            finish();
        });



    }
}