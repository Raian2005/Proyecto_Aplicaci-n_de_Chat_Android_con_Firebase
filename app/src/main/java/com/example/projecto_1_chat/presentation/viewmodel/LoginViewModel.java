package com.example.projecto_1_chat.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projecto_1_chat.data.repository.AuthRepositoryImpl;
import com.example.projecto_1_chat.domain.model.User;
import com.example.projecto_1_chat.domain.repository.AuthCallback;
import com.example.projecto_1_chat.domain.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> isLoginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LoginViewModel() {
        this.authRepository = new AuthRepositoryImpl();
    }

    public LiveData<Boolean> getIsLoginSuccess(){
        return isLoginSuccess;
    }

    public LiveData<String> getErrorMessage(){
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading(){
        return isLoading;
    }

    public void login(String email, String password){

        if(email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()){
            errorMessage.setValue("Todos los campos deben estar completados");
            return;
        }

        isLoading.setValue(true);

        authRepository.login(email, password, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                isLoading.setValue(false);
                isLoginSuccess.setValue(true);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
            }
        });
    }

    public void register(String name, String email, String password){
        if(name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()){
            errorMessage.setValue("Los campos nombre, email y password son obligatorios");
            return;
        }

        isLoading.setValue(true);

        authRepository.register(name, email, password, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                isLoading.setValue(false);
                isLoginSuccess.setValue(true);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
            }
        });
    }




}
