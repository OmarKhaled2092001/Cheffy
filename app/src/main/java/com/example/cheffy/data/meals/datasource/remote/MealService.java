package com.example.cheffy.data.meals.datasource.remote;

import com.example.cheffy.data.meals.models.AreaResponse;
import com.example.cheffy.data.meals.models.CategoryResponse;
import com.example.cheffy.data.meals.models.IngredientResponse;
import com.example.cheffy.data.meals.models.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("categories.php")
    Call<CategoryResponse> getCategories();

    @GET("list.php?a=list")
    Call<AreaResponse> getAreas();

    @GET("list.php?i=list")
    Call<IngredientResponse> getIngredients();

    @GET("search.php")
    Call<MealResponse> searchMealsByName(@Query("s") String name);

    @GET("filter.php")
    Call<MealResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealResponse> filterByArea(@Query("a") String area);

    @GET("filter.php")
    Call<MealResponse> filterByIngredient(@Query("i") String ingredient);

    @GET("lookup.php")
    Call<MealResponse> getMealById(@Query("i") String id);
}
