package com.example.projecto_1_chat.presentation.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projecto_1_chat.R;
import com.example.projecto_1_chat.databinding.ActivityLoginBinding;
import com.example.projecto_1_chat.presentation.viewmodel.LoginViewModel;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new LoginViewModel();

        if(loginViewModel.isUserLoggedIn()){
            Intent intent = new Intent(Login.this, App.class);
            startActivity(intent);
            finish();
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Observers();

        binding.buttonLogin.setOnClickListener(v -> {
            String email = binding.editTextTextEmailLogin.getText().toString().trim();
            String password = binding.editTextTextPasswordLogin.getText().toString().trim();

            loginViewModel.login(email, password);
        });

        binding.textViewClickRegister.setOnClickListener( v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });

    }

    private void Observers(){
        loginViewModel.getIsLoginSuccess().observe(this, result -> {
            if(result != null && result){
                Intent intent = new Intent(Login.this, App.class);
                startActivity(intent);
                finish();
            }
        });

        loginViewModel.getErrorMessage().observe(this, message -> {
            if(message != null){
                binding.editTextTextEmailLogin.setError(message);
                binding.editTextTextPasswordLogin.setError(message);
            }
        });
    }
}