package com.example.cheffy.data.auth.repository;

import com.example.cheffy.data.auth.models.User;

public interface AuthResultCallback {
    void onSuccess(User user);
    void onError(String message);
}
