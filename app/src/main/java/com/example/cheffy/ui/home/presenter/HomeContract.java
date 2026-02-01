package com.example.cheffy.ui.home.presenter;

import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.ui.plan.model.PlannedMeal;
import com.example.cheffy.ui.search.SearchFilterType;

import java.util.List;
import java.util.Set;

public interface HomeContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showMealOfTheDay(RemoteMeal meal);
        void showCategories(List<Category> categories);
        void showPopularMeals(List<RemoteMeal> meals);
        void showCuisines(List<Area> areas);
        void showIngredients(List<Ingredient> ingredients);
        void showPlannedMeal(List<PlannedMeal> meals);
        void hidePlannedMeal();
        void showError(String message);
        void navigateToMealDetails(RemoteMeal meal);
        void navigateToMealsList(String filter, SearchType type);
        void navigateToSearch(SearchFilterType filterType);
        void setUserName(String name);
        void showAddedToFavorites(String mealId);
        void showRemovedFromFavorites(String mealId);
        void setFavoriteIds(Set<String> favoriteIds);
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
        void onPopularMealFavoriteClicked(RemoteMeal meal);
    }
}
