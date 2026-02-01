package com.example.cheffy.data.auth.social;

public interface SocialAuthCallback {
    void onSuccess(String token);
    void onError(String message);
}
