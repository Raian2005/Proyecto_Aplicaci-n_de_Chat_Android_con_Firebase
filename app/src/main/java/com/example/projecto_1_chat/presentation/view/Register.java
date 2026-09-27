package com.example.projecto_1_chat.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projecto_1_chat.databinding.ActivityRegisterBinding;
import com.example.projecto_1_chat.presentation.viewmodel.LoginViewModel;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;

    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        loginViewModel = new LoginViewModel();

        Observers();

        binding.buttonRegister.setOnClickListener(v -> {

            String name = binding.editTextTextNombreRegister.getText().toString().trim();
            String email = binding.editTextTextEmailRegister.getText().toString().trim();
            String password = binding.editTextTextPasswordRegister.getText().toString().trim();

            loginViewModel.register(name, email, password);
        });

    }

    private void Observers(){
        loginViewModel.getIsLoginSuccess().observe(this, result -> {
            if(result != null && result){
                Intent intent = new Intent(Register.this, Chat.class);
                startActivity(intent);
                finish();
            }
        });

        loginViewModel.getErrorMessage().observe(this, error -> {
            if(error != null){
                Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}