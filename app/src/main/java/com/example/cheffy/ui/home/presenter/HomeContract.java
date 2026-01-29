package com.example.cheffy.ui.home.presenter;

import com.example.cheffy.data.meals.models.Area;
import com.example.cheffy.data.meals.models.Category;
import com.example.cheffy.data.meals.models.Ingredient;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;

import java.util.List;

public interface HomeContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showMealOfTheDay(RemoteMeal meal);
        void showCategories(List<Category> categories);
        void showPopularMeals(List<RemoteMeal> meals);
        void showCuisines(List<Area> areas);
        void showIngredients(List<Ingredient> ingredients);
        void showError(String message);
        void navigateToMealDetails(RemoteMeal meal);
        void navigateToMealsList(String filter, SearchType type);
        void setUserName(String name);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadHomeData();
        void onMealOfTheDayClicked(RemoteMeal meal);
        void onCategoryClicked(String category);
        void onCuisineClicked(String area);
        void onIngredientClicked(String ingredient);
        void onPopularMealClicked(RemoteMeal meal);
        void onTryAgainClicked();
    }
}
