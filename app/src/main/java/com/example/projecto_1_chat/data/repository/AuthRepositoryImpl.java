package com.example.projecto_1_chat.data.repository;

import com.example.projecto_1_chat.domain.model.User;
import com.example.projecto_1_chat.domain.repository.AuthCallback;
import com.example.projecto_1_chat.domain.repository.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepositoryImpl implements AuthRepository {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public AuthRepositoryImpl(){
        this.mAuth = FirebaseAuth.getInstance();
        this.mFirestore = FirebaseFirestore.getInstance();
    }


    @Override
    public void login(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if(firebaseUser != null){
                            User user = new User(firebaseUser.getUid(), "", firebaseUser.getEmail(), "");
                            callback.onSuccess(user);
                        }
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Error al iniciar sesion";
                        callback.onError(error);
                    }

        });

    }

    @Override
    public void register(String name, String email, String password, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       FirebaseUser firebaseUser = mAuth.getCurrentUser();
                       if(firebaseUser != null){
                           String uid = firebaseUser.getUid();
                           User user = new User(uid, name, email, "");

                           mFirestore.collection("users").document(uid).set(user)
                                   .addOnSuccessListener(aVoid -> callback.onSuccess(user))
                                   .addOnFailureListener(e -> callback.onError(e.getMessage()));
                       }
                   } else {
                       String error = task.getException() != null ? task.getException().getMessage() : "Error al registrar el usuario";
                       callback.onError(error);
                   }
                });
    }

    @Override
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    @Override
    public String getCurrentUserId() {

        if(isUserLoggedIn()){
            return mAuth.getCurrentUser().getUid();
        }
        return null;
    }
}
