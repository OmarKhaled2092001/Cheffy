package com.example.cheffy.data.auth.repository;

import com.example.cheffy.data.auth.datasource.remote.AuthRemoteDataSourceImpl;
import com.example.cheffy.data.auth.datasource.remote.IAuthRemoteDataSource;
import com.example.cheffy.data.auth.models.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    public Single<User> login(String email, String password) {
        return remoteDataSource.login(email, password)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<User> loginWithGoogle(String idToken) {
        return remoteDataSource.loginWithGoogle(idToken)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<User> register(String email, String password, String displayName) {
        return remoteDataSource.register(email, password, displayName)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable sendPasswordResetEmail(String email) {
        return remoteDataSource.sendPasswordResetEmail(email)
                .subscribeOn(Schedulers.io());
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
