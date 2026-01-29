package com.example.cheffy.ui.mealslist.presenter;

import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;

import java.util.List;

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
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadMeals(String filter, SearchType type);
        void onMealClicked(RemoteMeal meal);
        void onTryAgainClicked();
    }
}
