package com.example.cheffy.data.meals.repository;


public interface MealsDataCallback<T> {
    void onSuccess(T data);
    void onError(String message);
}
