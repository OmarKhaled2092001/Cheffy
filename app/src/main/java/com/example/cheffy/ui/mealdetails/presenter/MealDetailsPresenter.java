package com.example.cheffy.ui.mealdetails.presenter;

import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.data.meals.repository.MealsDataCallback;
import com.example.cheffy.ui.mealdetails.model.IngredientItem;

import java.util.List;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private MealDetailsContract.View view;
    private final IMealsRepository mealsRepository;

    public MealDetailsPresenter(IMealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

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

        if (isPartialMeal(meal)) {
            fetchFullMealDetails(meal.getIdMeal());
        } else {
            displayMealDetails(meal);
        }
    }

    private boolean isPartialMeal(RemoteMeal meal) {
        return meal.getInstructions() == null || meal.getInstructions().isEmpty();
    }

    private void fetchFullMealDetails(String mealId) {
        if (view == null) return;

        mealsRepository.getMealById(mealId, new MealsDataCallback<RemoteMeal>() {
            @Override
            public void onSuccess(RemoteMeal fullMeal) {
                if (view == null) return;
                displayMealDetails(fullMeal);
            }

            @Override
            public void onError(String message) {
                if (view == null) return;
                view.showError(message != null ? message : "Failed to load meal details");
            }
        });
    }

    private void displayMealDetails(RemoteMeal meal) {
        if (view == null) return;

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
