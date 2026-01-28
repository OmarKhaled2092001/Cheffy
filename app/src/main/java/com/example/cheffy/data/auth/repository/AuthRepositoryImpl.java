package com.example.cheffy.data.auth.repository;

import com.example.cheffy.data.auth.datasource.remote.AuthRemoteDataSourceImpl;
import com.example.cheffy.data.auth.datasource.remote.IAuthRemoteDataSource;
import com.example.cheffy.data.auth.models.User;

public class AuthRepositoryImpl implements IAuthRepository {
    private final IAuthRemoteDataSource remoteDataSource;
    private static AuthRepositoryImpl instance;

    private AuthRepositoryImpl(IAuthRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized AuthRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new AuthRepositoryImpl(AuthRemoteDataSourceImpl.getInstance());
        }
        return instance;
    }

    @Override
    public void login(String email, String password, AuthResultCallback callback) {
        remoteDataSource.login(email, password, callback);
    }

    @Override
    public void loginWithGoogle(String idToken, AuthResultCallback callback) {
        remoteDataSource.loginWithGoogle(idToken, callback);
    }

    @Override
    public void register(String email, String password, String displayName, AuthResultCallback callback) {
        remoteDataSource.register(email, password, displayName, callback);
    }

    @Override
    public void sendPasswordResetEmail(String email, AuthResultCallback callback) {
        remoteDataSource.sendPasswordResetEmail(email, callback);
    }

    @Override
    public boolean isUserLoggedIn() {
        return remoteDataSource.isUserLoggedIn();
    }

    @Override
    public User getCurrentUser() {
        return remoteDataSource.getCurrentUser();
    }
}
