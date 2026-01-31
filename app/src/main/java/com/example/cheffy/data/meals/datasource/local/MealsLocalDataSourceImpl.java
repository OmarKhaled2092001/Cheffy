package com.example.cheffy.data.meals.datasource.local;

import android.content.Context;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealsLocalDataSourceImpl implements IMealsLocalDataSource {

    private final FavoriteMealDao favoriteMealDao;
    private static MealsLocalDataSourceImpl instance;

    private MealsLocalDataSourceImpl(Context context) {
        MealsDatabase database = MealsDatabase.getInstance(context);
        this.favoriteMealDao = database.favoriteMealDao();
    }

    public static synchronized MealsLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealsLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public Flowable<List<FavoriteMealEntity>> observeFavorites() {
        return favoriteMealDao.observeAllFavorites();
    }

    @Override
    public Completable addFavorite(FavoriteMealEntity meal) {
        return favoriteMealDao.insertFavorite(meal);
    }

    @Override
    public Completable removeFavorite(FavoriteMealEntity meal) {
        return favoriteMealDao.deleteFavorite(meal);
    }

    @Override
    public Completable removeFavoriteById(String mealId) {
        return favoriteMealDao.deleteFavoriteById(mealId);
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        return favoriteMealDao.isFavorite(mealId);
    }
}
