package com.example.cheffy.ui.mealdetails.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.ui.mealdetails.model.IngredientItem;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MealDetailsPresenter extends BasePresenter<MealDetailsContract.View> implements MealDetailsContract.Presenter {

    private final IMealsRepository mealsRepository;

    public MealDetailsPresenter(IMealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @Override
    public void attachView(MealDetailsContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loadMealDetails(RemoteMeal meal) {
        if (!isViewAttached()) return;

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
        if (!isViewAttached()) return;

        addDisposable(
            mealsRepository.getMealById(mealId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    fullMeal -> {
                        if (isViewAttached()) {
                            displayMealDetails(fullMeal);
                        }
                    },
                    throwable -> {
                        if (isViewAttached()) {
                            view.showError(throwable.getMessage() != null 
                                ? throwable.getMessage() 
                                : "Failed to load meal details");
                        }
                    }
                )
        );
    }

    private void displayMealDetails(RemoteMeal meal) {
        if (!isViewAttached()) return;

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
        if (!isViewAttached()) return;
        view.showAddToFavoritesMessage();
    }
}
