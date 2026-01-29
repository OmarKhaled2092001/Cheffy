package com.example.cheffy.ui.mealdetails.presenter;

import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.ui.mealdetails.model.IngredientItem;

import java.util.List;

public interface MealDetailsContract {

    interface View {
        void showMealDetails(RemoteMeal meal);
        void showIngredients(List<IngredientItem> ingredients);
        void showInstructions(String instructions, String youtubeUrl);
        void showAddToFavoritesMessage();
        void showError(String message);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadMealDetails(RemoteMeal meal);
        void onAddToFavoritesClicked();
    }
}
