package com.example.cheffy.data.meals.repository;

import android.content.Context;
import android.util.Log;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;
import com.example.cheffy.data.meals.models.local.MealPlanEntity;
import com.example.cheffy.data.meals.datasource.cloud.IMealsCloudDataSource;
import com.example.cheffy.data.meals.datasource.cloud.MealsCloudDataSourceImpl;
import com.example.cheffy.data.meals.datasource.local.IMealsLocalDataSource;
import com.example.cheffy.data.meals.datasource.local.MealsLocalDataSourceImpl;
import com.example.cheffy.data.meals.datasource.remote.MealsRemoteDataSourceImpl;
import com.example.cheffy.data.meals.datasource.remote.IMealsRemoteDataSource;
import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.ui.plan.model.PlannedMeal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepositoryImpl implements IMealsRepository {

    private static final String TAG = "MealsRepository";

    private final IMealsRemoteDataSource remoteDataSource;
    private final IMealsLocalDataSource localDataSource;
    private final IMealsCloudDataSource cloudDataSource;
    private static MealsRepositoryImpl instance;

    private MealsRepositoryImpl(IMealsRemoteDataSource remoteDataSource, 
                                 IMealsLocalDataSource localDataSource,
                                 IMealsCloudDataSource cloudDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.cloudDataSource = cloudDataSource;
    }

    public static synchronized MealsRepositoryImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealsRepositoryImpl(
                    MealsRemoteDataSourceImpl.getInstance(),
                    MealsLocalDataSourceImpl.getInstance(context),
                    MealsCloudDataSourceImpl.getInstance()
            );
        }
        return instance;
    }

    // ================== API Methods ==================

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
                .map(this::mapFavoriteEntitiesToMeals);
    }

    @Override
    public Completable addFavorite(RemoteMeal meal) {
        FavoriteMealEntity entity = FavoriteMealEntity.fromRemoteMeal(meal);
        return localDataSource.addFavorite(entity)
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> fireAndForgetUploadFavorite(entity));
    }

    @Override
    public Completable removeFavorite(RemoteMeal meal) {
        return localDataSource.removeFavoriteById(meal.getIdMeal())
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> fireAndForgetDeleteFavorite(meal.getIdMeal()));
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        return localDataSource.isFavorite(mealId)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<PlannedMeal>> observeMealPlanByDay(String dayOfWeek) {
        return localDataSource.observeMealPlansByDay(dayOfWeek)
                .subscribeOn(Schedulers.io())
                .map(this::mapPlanEntitiesToPlannedMeals);
    }

    @Override
    public Completable addMealToPlan(RemoteMeal meal, String dayOfWeek) {
        MealPlanEntity entity = MealPlanEntity.fromRemoteMeal(meal, dayOfWeek);
        return localDataSource.addMealToPlan(entity)
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> fireAndForgetUploadMealPlan(entity));
    }


    @Override
    public Completable removeMealFromPlanWithSync(String mealId, String dayOfWeek, long planId) {
        return localDataSource.removeMealFromPlan(planId)
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> fireAndForgetDeleteMealPlan(mealId, dayOfWeek));
    }


    @Override
    public Completable restoreDataFromCloud() {
        String uid = getCurrentUserId();
        if (uid == null) {
            return Completable.complete();
        }

        Single<List<FavoriteMealEntity>> favoritesSingle = cloudDataSource.fetchAllFavorites(uid);
        Single<List<MealPlanEntity>> plansSingle = cloudDataSource.fetchAllMealPlans(uid);

        return Single.zip(favoritesSingle, plansSingle, CloudData::new)
        .subscribeOn(Schedulers.io())
        .flatMapCompletable(cloudData -> {
            Completable insertFavorites = cloudData.favorites.isEmpty() 
                    ? Completable.complete() 
                    : localDataSource.insertAllFavorites(cloudData.favorites);
            Completable insertPlans = cloudData.plans.isEmpty() 
                    ? Completable.complete() 
                    : localDataSource.insertAllMealPlans(cloudData.plans);
            
            return Completable.mergeArray(insertFavorites, insertPlans);
        });
    }

    @Override
    public Completable backupLocalDataToCloud() {
        String uid = getCurrentUserId();
        if (uid == null) {
            return Completable.error(new IllegalStateException("User not logged in"));
        }

        Single<List<FavoriteMealEntity>> localFavorites = localDataSource.getAllFavorites();
        Single<List<MealPlanEntity>> localPlans = localDataSource.getAllMealPlans();

        return Single.zip(localFavorites, localPlans, CloudData::new)
        .subscribeOn(Schedulers.io())
        .flatMapCompletable(localData -> {
            Completable uploadFavorites = localData.favorites.isEmpty() 
                    ? Completable.complete() 
                    : cloudDataSource.uploadAllFavorites(uid, localData.favorites);
            Completable uploadPlans = localData.plans.isEmpty() 
                    ? Completable.complete() 
                    : cloudDataSource.uploadAllMealPlans(uid, localData.plans);
            
            return Completable.mergeArray(uploadFavorites, uploadPlans);
        });
    }

    @Override
    public Single<Boolean> hasCloudData() {
        String uid = getCurrentUserId();
        if (uid == null) {
            return Single.just(false);
        }
        return cloudDataSource.hasCloudData(uid)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable clearAllLocalData() {
        return Completable.mergeArray(
                localDataSource.clearAllFavorites(),
                localDataSource.clearAllMealPlans()
        ).subscribeOn(Schedulers.io());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void fireAndForgetUploadFavorite(FavoriteMealEntity entity) {
        String uid = getCurrentUserId();
        if (uid == null) return;
        
        cloudDataSource.uploadFavorite(uid, entity)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {},
                        e -> Log.e(TAG, "Failed to sync favorite", e)
                );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void fireAndForgetDeleteFavorite(String mealId) {
        String uid = getCurrentUserId();
        if (uid == null) return;
        
        cloudDataSource.deleteFavorite(uid, mealId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {},
                        e -> Log.e(TAG, "Failed to delete favorite from cloud", e)
                );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void fireAndForgetUploadMealPlan(MealPlanEntity entity) {
        String uid = getCurrentUserId();
        if (uid == null) return;
        
        cloudDataSource.uploadMealPlan(uid, entity)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {},
                        e -> Log.e(TAG, "Failed to sync meal plan", e)
                );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void fireAndForgetDeleteMealPlan(String mealId, String dayOfWeek) {
        String uid = getCurrentUserId();
        if (uid == null) return;
        
        cloudDataSource.deleteMealPlan(uid, mealId, dayOfWeek)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {},
                        e -> Log.e(TAG, "Failed to delete meal plan from cloud", e)
                );
    }


    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    private List<RemoteMeal> mapFavoriteEntitiesToMeals(List<FavoriteMealEntity> entities) {
        List<RemoteMeal> meals = new ArrayList<>();
        for (FavoriteMealEntity entity : entities) {
            meals.add(entity.toRemoteMeal());
        }
        return meals;
    }

    private List<PlannedMeal> mapPlanEntitiesToPlannedMeals(List<MealPlanEntity> entities) {
        List<PlannedMeal> plannedMeals = new ArrayList<>();
        for (MealPlanEntity entity : entities) {
            plannedMeals.add(new PlannedMeal(
                    entity.getId(),
                    entity.toRemoteMeal(),
                    entity.getDayOfWeek()
            ));
        }
        return plannedMeals;
    }

    private static class CloudData {
        final List<FavoriteMealEntity> favorites;
        final List<MealPlanEntity> plans;

        CloudData(List<FavoriteMealEntity> favorites, List<MealPlanEntity> plans) {
            this.favorites = favorites;
            this.plans = plans;
        }
    }
}
