package com.example.cheffy.data.meals.datasource.remote;

import androidx.annotation.NonNull;

import com.example.cheffy.data.meals.models.Area;
import com.example.cheffy.data.meals.models.AreaResponse;
import com.example.cheffy.data.meals.models.Category;
import com.example.cheffy.data.meals.models.CategoryResponse;
import com.example.cheffy.data.meals.models.Ingredient;
import com.example.cheffy.data.meals.models.IngredientResponse;
import com.example.cheffy.data.meals.models.MealResponse;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.repository.MealsDataCallback;
import com.example.cheffy.network.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void getRandomMeal(MealsDataCallback<RemoteMeal> callback) {
        mealService.getRandomMeal().enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RemoteMeal> meals = response.body().getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        callback.onSuccess(meals.get(0));
                    } else {
                        callback.onError("No meal found");
                    }
                } else {
                    callback.onError("Failed to fetch random meal");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        });
    }

    @Override
    public void getCategories(MealsDataCallback<List<Category>> callback) {
        mealService.getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getCategories();
                    if (categories != null) {
                        callback.onSuccess(categories);
                    } else {
                        callback.onError("No categories found");
                    }
                } else {
                    callback.onError("Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        });
    }

    @Override
    public void getAreas(MealsDataCallback<List<Area>> callback) {
        mealService.getAreas().enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(@NonNull Call<AreaResponse> call, @NonNull Response<AreaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Area> areas = response.body().getAreas();
                    if (areas != null) {
                        callback.onSuccess(areas);
                    } else {
                        callback.onError("No areas found");
                    }
                } else {
                    callback.onError("Failed to fetch areas");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AreaResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        });
    }

    @Override
    public void getIngredients(MealsDataCallback<List<Ingredient>> callback) {
        mealService.getIngredients().enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(@NonNull Call<IngredientResponse> call, @NonNull Response<IngredientResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ingredient> ingredients = response.body().getIngredients();
                    if (ingredients != null) {
                        int limit = Math.min(ingredients.size(), 20);
                        callback.onSuccess(ingredients.subList(0, limit));
                    } else {
                        callback.onError("No ingredients found");
                    }
                } else {
                    callback.onError("Failed to fetch ingredients");
                }
            }

            @Override
            public void onFailure(@NonNull Call<IngredientResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        });
    }

    @Override
    public void getPopularMeals(int count, MealsDataCallback<List<RemoteMeal>> callback) {
        List<RemoteMeal> popularMeals = new ArrayList<>();
        AtomicInteger completedCalls = new AtomicInteger(0);

        for (int i = 0; i < count; i++) {
            mealService.getRandomMeal().enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                    synchronized (popularMeals) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<RemoteMeal> meals = response.body().getMeals();
                            if (meals != null && !meals.isEmpty()) {
                                popularMeals.add(meals.get(0));
                            }
                        }

                        if (completedCalls.incrementAndGet() == count) {
                            if (!popularMeals.isEmpty()) {
                                callback.onSuccess(popularMeals);
                            } else {
                                callback.onError("Failed to fetch popular meals");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                    synchronized (popularMeals) {
                        if (completedCalls.incrementAndGet() == count) {
                            if (!popularMeals.isEmpty()) {
                                callback.onSuccess(popularMeals);
                            } else {
                                callback.onError("Network error fetching popular meals");
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void getMealsByFilter(SearchType type, String filter, MealsDataCallback<List<RemoteMeal>> callback) {
        Call<MealResponse> call;

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
                callback.onError("Unknown filter type");
                return;
        }

        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RemoteMeal> meals = response.body().getMeals();
                    if (meals != null) {
                        callback.onSuccess(meals);
                    } else {
                        callback.onSuccess(new ArrayList<>());
                    }
                } else {
                    callback.onError("Failed to fetch meals");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        });
    }

    @Override
    public void getMealById(String mealId, MealsDataCallback<RemoteMeal> callback) {
        mealService.getMealById(mealId).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RemoteMeal> meals = response.body().getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        callback.onSuccess(meals.get(0));
                    } else {
                        callback.onError("Meal not found");
                    }
                } else {
                    callback.onError("Failed to fetch meal details");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        });
    }
}
