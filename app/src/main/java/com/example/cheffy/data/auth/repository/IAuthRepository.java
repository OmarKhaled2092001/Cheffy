package com.example.cheffy.data.auth.repository;

import com.example.cheffy.data.auth.models.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface IAuthRepository {
    Single<User> login(String email, String password);
    
    Single<User> loginWithGoogle(String idToken);
    
    Single<User> register(String email, String password, String displayName);
    
    Completable sendPasswordResetEmail(String email);
    
    boolean isUserLoggedIn();
    
    User getCurrentUser();
}
