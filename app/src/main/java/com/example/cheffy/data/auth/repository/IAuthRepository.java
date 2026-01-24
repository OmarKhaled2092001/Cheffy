package com.example.cheffy.data.auth.repository;

import com.example.cheffy.data.auth.models.User;

public interface IAuthRepository {
    void login(String email, String password, AuthResultCallback callback);
    void loginWithGoogle(String idToken, AuthResultCallback callback);
    void register(String email, String password, String displayName, AuthResultCallback callback);
    void sendPasswordResetEmail(String email, AuthResultCallback callback);
    boolean isUserLoggedIn();
    User getCurrentUser();
}
