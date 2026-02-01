package com.example.cheffy.ui.mealslist.presenter;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;

import java.util.List;
import java.util.Set;

public interface MealsListContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showMeals(List<RemoteMeal> meals);
        void showError(String message);
        void showEmpty();
        void navigateToMealDetails(RemoteMeal meal);
        void setTitle(String title);
        void setSubtitle(String subtitle);
        void showAddedToFavorites(String mealId);
        void showRemovedFromFavorites(String mealId);
        void setFavoriteIds(Set<String> favoriteIds);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadMeals(String filter, SearchType type);
        void onMealClicked(RemoteMeal meal);
        void onTryAgainClicked();
        void onMealFavoriteClicked(RemoteMeal meal);
    }
}
