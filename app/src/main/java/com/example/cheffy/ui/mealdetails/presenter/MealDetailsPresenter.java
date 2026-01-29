package com.example.cheffy.ui.mealdetails.presenter;

import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.ui.mealdetails.model.IngredientItem;

import java.util.List;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private MealDetailsContract.View view;

    @Override
    public void attachView(MealDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadMealDetails(RemoteMeal meal) {
        if (view == null) return;

        if (meal == null) {
            view.showError("Unable to load meal details");
            return;
        }

        try {
            List<IngredientItem> ingredientsList = meal.getIngredientsList();

            view.showMealDetails(meal);
            view.showIngredients(ingredientsList);
            view.showInstructions(meal.getInstructions(), meal.getYoutube());
        } catch (Exception e) {
            view.showError("Error loading meal: " + e.getMessage());
        }
    }

    @Override
    public void onAddToFavoritesClicked() {
        if (view == null) return;
        view.showAddToFavoritesMessage();
    }
}

