package com.example.cheffy.data.meals.repository;

import com.example.cheffy.data.meals.datasource.remote.MealsRemoteDataSourceImpl;
import com.example.cheffy.data.meals.datasource.remote.IMealsRemoteDataSource;
import com.example.cheffy.data.meals.models.Area;
import com.example.cheffy.data.meals.models.Category;
import com.example.cheffy.data.meals.models.Ingredient;
import com.example.cheffy.data.meals.models.RemoteMeal;

import java.util.List;

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
    public void getRandomMeal(MealsDataCallback<RemoteMeal> callback) {
        remoteDataSource.getRandomMeal(callback);
    }

    @Override
    public void getCategories(MealsDataCallback<List<Category>> callback) {
        remoteDataSource.getCategories(callback);
    }

    @Override
    public void getAreas(MealsDataCallback<List<Area>> callback) {
        remoteDataSource.getAreas(callback);
    }

    @Override
    public void getIngredients(MealsDataCallback<List<Ingredient>> callback) {
        remoteDataSource.getIngredients(callback);
    }

    @Override
    public void getPopularMeals(int count, MealsDataCallback<List<RemoteMeal>> callback) {
        remoteDataSource.getPopularMeals(count, callback);
    }
}
