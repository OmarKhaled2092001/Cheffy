package com.example.cheffy.ui.search.presenter;

import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.ui.search.SearchFilterType;

import java.util.List;
import java.util.Set;

public interface SearchContract {

    interface View {
        void showLoading();
        void hideLoading();
        void renderMeals(List<RemoteMeal> meals);
        void renderCategories(List<Category> categories);
        void renderAreas(List<Area> areas);
        void renderIngredients(List<Ingredient> ingredients);
        void showEmptyState(String message);
        void showError(String message);
        void hideError();
        void selectChip(SearchFilterType type);
        void navigateToMealDetails(RemoteMeal meal);
        void navigateToMealsList(String filter, SearchType type);
        void showAddedToFavorites(String mealId);
        void showRemovedFromFavorites(String mealId);
        void setFavoriteIds(Set<String> favoriteIds);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadInitialData(SearchFilterType initialFilter);
        void onChipSelected(SearchFilterType type);
        void onSearchQueryChanged(String query);
        void onMealClicked(RemoteMeal meal);
        void onCategoryClicked(Category category);
        void onAreaClicked(Area area);
        void onIngredientClicked(Ingredient ingredient);
        void onMealFavoriteClicked(RemoteMeal meal);
    }
}

