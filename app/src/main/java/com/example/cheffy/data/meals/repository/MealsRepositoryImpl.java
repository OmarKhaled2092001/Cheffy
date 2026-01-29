package com.example.cheffy.data.meals.repository;

import com.example.cheffy.data.meals.datasource.remote.MealsRemoteDataSourceImpl;
import com.example.cheffy.data.meals.datasource.remote.IMealsRemoteDataSource;
import com.example.cheffy.data.meals.models.Area;
import com.example.cheffy.data.meals.models.Category;
import com.example.cheffy.data.meals.models.Ingredient;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepositoryImpl implements IMealsRepository {

    private final IMealsRemoteDataSource remoteDataSource;
    private static MealsRepositoryImpl instance;

    private MealsRepositoryImpl(IMealsRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized MealsRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new MealsRepositoryImpl(MealsRemoteDataSourceImpl.getInstance());
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
    public Single<RemoteMeal> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId)
                .subscribeOn(Schedulers.io());
    }
}
