package com.example.cheffy.data.meals.datasource.local;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


public interface IMealsLocalDataSource {

    Flowable<List<FavoriteMealEntity>> observeFavorites();

    Completable addFavorite(FavoriteMealEntity meal);

    Completable removeFavorite(FavoriteMealEntity meal);

    Completable removeFavoriteById(String mealId);

    Single<Boolean> isFavorite(String mealId);
}
