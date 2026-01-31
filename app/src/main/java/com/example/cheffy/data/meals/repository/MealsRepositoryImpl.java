package com.example.cheffy.data.meals.repository;

import android.content.Context;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;
import com.example.cheffy.data.meals.datasource.local.IMealsLocalDataSource;
import com.example.cheffy.data.meals.datasource.local.MealsLocalDataSourceImpl;
import com.example.cheffy.data.meals.datasource.remote.MealsRemoteDataSourceImpl;
import com.example.cheffy.data.meals.datasource.remote.IMealsRemoteDataSource;
import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepositoryImpl implements IMealsRepository {

    private final IMealsRemoteDataSource remoteDataSource;
    private final IMealsLocalDataSource localDataSource;
    private static MealsRepositoryImpl instance;

    private MealsRepositoryImpl(IMealsRemoteDataSource remoteDataSource, 
                                 IMealsLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static synchronized MealsRepositoryImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealsRepositoryImpl(
                    MealsRemoteDataSourceImpl.getInstance(),
                    MealsLocalDataSourceImpl.getInstance(context)
            );
        }
        return instance;
    }

    @Override
    public Single<RemoteMeal> getRandomMeal() {
        return remoteDataSource.getRandomMeal()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Category>> getCategories() {
        return remoteDataSource.getCategories()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Area>> getAreas() {
        return remoteDataSource.getAreas()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Ingredient>> getIngredients() {
        return remoteDataSource.getIngredients()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<RemoteMeal>> getPopularMeals(int count) {
        return remoteDataSource.getPopularMeals(count)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<RemoteMeal>> getMealsByFilter(SearchType type, String filter) {
        return remoteDataSource.getMealsByFilter(type, filter)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<RemoteMeal>> searchMealsByName(String query) {
        return remoteDataSource.searchMealsByName(query)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<RemoteMeal>> searchMealsByFirstLetter(String letter) {
        return remoteDataSource.searchMealsByFirstLetter(letter)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<RemoteMeal> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<RemoteMeal>> observeFavorites() {
        return localDataSource.observeFavorites()
                .subscribeOn(Schedulers.io())
                .map(this::mapEntitiesToMeals);
    }

    @Override
    public Completable addFavorite(RemoteMeal meal) {
        FavoriteMealEntity entity = FavoriteMealEntity.fromRemoteMeal(meal);
        return localDataSource.addFavorite(entity)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeFavorite(RemoteMeal meal) {
        return localDataSource.removeFavoriteById(meal.getIdMeal())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        return localDataSource.isFavorite(mealId)
                .subscribeOn(Schedulers.io());
    }


    private List<RemoteMeal> mapEntitiesToMeals(List<FavoriteMealEntity> entities) {
        List<RemoteMeal> meals = new ArrayList<>();
        for (FavoriteMealEntity entity : entities) {
            meals.add(entity.toRemoteMeal());
        }
        return meals;
    }
}

