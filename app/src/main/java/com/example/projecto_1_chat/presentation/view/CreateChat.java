package com.example.projecto_1_chat.presentation.view;

import android.os.Bundle;
import android.widget.Toast;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projecto_1_chat.databinding.ActivityCreatechatBinding;
import com.example.projecto_1_chat.presentation.viewmodel.ChatListViewModel;
import com.example.projecto_1_chat.presentation.viewmodel.LoginViewModel;

public class CreateChat extends AppCompatActivity {

    ActivityCreatechatBinding binding;

    LoginViewModel loginViewModel;
    ChatListViewModel chatListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidx.activity.EdgeToEdge.enable(this);

        binding = ActivityCreatechatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginViewModel = new LoginViewModel();
        chatListViewModel = new ChatListViewModel();

        chatListViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(CreateChat.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        chatListViewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(CreateChat.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        binding.crearChat.setOnClickListener(v -> {
            String email = binding.editTextTextEmailLogin
                    .getText()
                    .toString()
                    .trim();

            chatListViewModel.createChatByEmail(
                    loginViewModel.getCurrentUserId(),
                    email
            );
        });
    }
}
