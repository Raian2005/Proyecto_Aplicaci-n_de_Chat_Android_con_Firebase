package com.example.projecto_1_chat.presentation.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projecto_1_chat.R;
import com.example.projecto_1_chat.databinding.ActivityAppBinding;
import com.example.projecto_1_chat.presentation.viewmodel.LoginViewModel;

public class App extends AppCompatActivity {

    ActivityAppBinding binding;

    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new LoginViewModel();

        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarClickLogout.setOnClickListener(v -> {
            loginViewModel.logout();
            Intent intent = new Intent(App.this, Login.class);
            startActivity(intent);
            finish();
        });

    }
}