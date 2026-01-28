package com.example.cheffy.data.auth.datasource.remote;

import com.example.cheffy.data.auth.models.User;
import com.example.cheffy.data.auth.repository.AuthResultCallback;

public interface IAuthRemoteDataSource {
    void login(String email, String password, AuthResultCallback callback);

    void loginWithGoogle(String idToken, AuthResultCallback callback);

    void register(String email, String password, String displayName, AuthResultCallback callback);

    void sendPasswordResetEmail(String email, AuthResultCallback callback);

    boolean isUserLoggedIn();

    User getCurrentUser();
}
