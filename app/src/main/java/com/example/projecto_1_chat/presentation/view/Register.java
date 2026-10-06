package com.example.projecto_1_chat.presentation.view;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projecto_1_chat.databinding.ActivityRegisterBinding;
import com.example.projecto_1_chat.presentation.utils.ImageBase64;
import com.example.projecto_1_chat.presentation.viewmodel.LoginViewModel;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;

    LoginViewModel loginViewModel;
    private String profileImageBase64 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidx.activity.EdgeToEdge.enable(this);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginViewModel = new LoginViewModel();

        Observers();

        binding.buttonRegister.setOnClickListener(v -> {

            String name = binding.editTextTextNombreRegister.getText().toString().trim();
            String email = binding.editTextTextEmailRegister.getText().toString().trim();
            String password = binding.editTextTextPasswordRegister.getText().toString().trim();

            loginViewModel.register(name, email, password, profileImageBase64);
        });

        binding.buttonSelectProfileImage.setOnClickListener(v -> selectImageGallery.launch("image/*"));
    }

    private final ActivityResultLauncher<String> selectImageGallery =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri == null) {
                    return;
                }

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profileImageBase64 = ImageBase64.bitmapToBase64(bitmap);
                    binding.buttonSelectProfileImage.setText("Imagen seleccionada");
                } catch (Exception e) {
                    Toast.makeText(Register.this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            });

    private void Observers(){
        loginViewModel.getIsLoginSuccess().observe(this, result -> {
            if(result != null && result){
                Intent intent = new Intent(Register.this, App.class);
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