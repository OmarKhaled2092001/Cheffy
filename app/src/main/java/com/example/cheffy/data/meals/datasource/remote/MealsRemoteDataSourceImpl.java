package com.example.cheffy.data.meals.datasource.remote;

import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.MealResponse;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.network.Network;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealsRemoteDataSourceImpl implements IMealsRemoteDataSource {

    private final MealService mealService;
    private static MealsRemoteDataSourceImpl instance;

    private MealsRemoteDataSourceImpl() {
        this.mealService = Network.getInstance().getMealService();
    }

    public static synchronized MealsRemoteDataSourceImpl getInstance() {
        if (instance == null) {
            instance = new MealsRemoteDataSourceImpl();
        }
        return instance;
    }

    @Override
    public Single<RemoteMeal> getRandomMeal() {
        return mealService.getRandomMeal()
                .map(response -> {
                    if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                        return response.getMeals().get(0);
                    }
                    throw new RuntimeException("No meal found");
                });
    }

    @Override
    public Single<List<Category>> getCategories() {
        return mealService.getCategories()
                .map(response -> {
                    if (response.getCategories() != null) {
                        return response.getCategories();
                    }
                    throw new RuntimeException("No categories found");
                });
    }

    @Override
    public Single<List<Area>> getAreas() {
        return mealService.getAreas()
                .map(response -> {
                    if (response.getAreas() != null) {
                        return response.getAreas();
                    }
                    throw new RuntimeException("No areas found");
                });
    }

    @Override
    public Single<List<Ingredient>> getIngredients() {
        return mealService.getIngredients()
                .map(response -> {
                    if (response.getIngredients() != null) {
                        int limit = Math.min(response.getIngredients().size(), 20);
                        return response.getIngredients().subList(0, limit);
                    }
                    throw new RuntimeException("No ingredients found");
                });
    }

    @Override
    public Single<List<RemoteMeal>> getPopularMeals(int count) {
        return Observable.range(0, count)
                .flatMapSingle(i -> mealService.getRandomMeal()
                        .map(response -> {
                            if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                return response.getMeals().get(0);
                            }
                            return null;
                        })
                        .onErrorReturnItem(new RemoteMeal())
                )
                .filter(meal -> meal != null && meal.getIdMeal() != null)
                .distinct(RemoteMeal::getIdMeal)
                .toList();
    }

    @Override
    public Single<List<RemoteMeal>> getMealsByFilter(SearchType type, String filter) {
        Single<MealResponse> call;

        switch (type) {
            case CATEGORY:
                call = mealService.filterByCategory(filter);
                break;
            case AREA:
                call = mealService.filterByArea(filter);
                break;
            case INGREDIENT:
                call = mealService.filterByIngredient(filter);
                break;
            default:
                return Single.error(new RuntimeException("Unknown filter type"));
        }

        return call.map(response -> {
            if (response.getMeals() != null) {
                return response.getMeals();
            }
            return new ArrayList<>();
        });
    }

    @Override
    public Single<RemoteMeal> getMealById(String mealId) {
        return mealService.getMealById(mealId)
                .map(response -> {
                    if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                        return response.getMeals().get(0);
                    }
                    throw new RuntimeException("Meal not found");
                });
    }

    @Override
    public Single<List<RemoteMeal>> searchMealsByName(String query) {
        return mealService.searchMealsByName(query)
                .map(response -> {
                    if (response.getMeals() != null) {
                        return response.getMeals();
                    }
                    return new ArrayList<>();
                });
    }

    @Override
    public Single<List<RemoteMeal>> searchMealsByFirstLetter(String letter) {
        return mealService.searchMealsByFirstLetter(letter)
                .map(response -> {
                    if (response.getMeals() != null) {
                        return response.getMeals();
                    }
                    return new ArrayList<>();
                });
    }
}
