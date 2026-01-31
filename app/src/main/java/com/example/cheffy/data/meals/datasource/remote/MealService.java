package com.example.cheffy.data.meals.datasource.remote;

import com.example.cheffy.data.meals.models.remote.AreaResponse;
import com.example.cheffy.data.meals.models.remote.CategoryResponse;
import com.example.cheffy.data.meals.models.remote.IngredientResponse;
import com.example.cheffy.data.meals.models.remote.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("list.php?a=list")
    Single<AreaResponse> getAreas();

    @GET("list.php?i=list")
    Single<IngredientResponse> getIngredients();

    @GET("search.php")
    Single<MealResponse> searchMealsByName(@Query("s") String name);

    @GET("search.php")
    Single<MealResponse> searchMealsByFirstLetter(@Query("f") String firstLetter);

    @GET("filter.php")
    Single<MealResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<MealResponse> filterByArea(@Query("a") String area);

    @GET("filter.php")
    Single<MealResponse> filterByIngredient(@Query("i") String ingredient);

    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String id);
}
