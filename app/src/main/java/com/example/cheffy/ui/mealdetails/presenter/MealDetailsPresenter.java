package com.example.cheffy.ui.mealdetails.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.ui.mealdetails.model.IngredientItem;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MealDetailsPresenter extends BasePresenter<MealDetailsContract.View> implements MealDetailsContract.Presenter {

    private final IMealsRepository mealsRepository;
    private RemoteMeal currentMeal;
    private boolean isFavorite = false;

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

        this.currentMeal = meal;

        if (isPartialMeal(meal)) {
            fetchFullMealDetails(meal.getIdMeal());
        } else {
            displayMealDetails(meal);
        }
        
        checkFavoriteStatus(meal.getIdMeal());
    }

    private void checkFavoriteStatus(String mealId) {
        addDisposable(
            mealsRepository.isFavorite(mealId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    isFav -> {
                        this.isFavorite = isFav;
                        if (isViewAttached()) {
                            view.updateFavoriteIcon(isFav);
                        }
                    },
                    throwable -> {
                        this.isFavorite = false;
                    }
                )
        );
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
                            this.currentMeal = fullMeal;
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
        if (!isViewAttached() || currentMeal == null) return;
        
        if (isFavorite) {
            addDisposable(
                mealsRepository.removeFavorite(currentMeal)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        () -> {
                            isFavorite = false;
                            if (isViewAttached()) {
                                view.updateFavoriteIcon(false);
                                view.showRemovedFromFavoritesMessage();
                            }
                        },
                        throwable -> {
                            if (isViewAttached()) {
                                view.showError("Failed to remove from favorites");
                            }
                        }
                    )
            );
        } else {
            addDisposable(
                mealsRepository.addFavorite(currentMeal)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        () -> {
                            isFavorite = true;
                            if (isViewAttached()) {
                                view.updateFavoriteIcon(true);
                                view.showAddToFavoritesMessage();
                            }
                        },
                        throwable -> {
                            if (isViewAttached()) {
                                view.showError("Failed to add to favorites");
                            }
                        }
                    )
            );
        }
    }

    @Override
    public void onAddToPlanClicked() {
        if (!isViewAttached() || currentMeal == null) return;
        view.showDayPicker();
    }

    @Override
    public void addMealToPlan(String dayOfWeek) {
        if (!isViewAttached() || currentMeal == null) return;

        addDisposable(
            mealsRepository.addMealToPlan(currentMeal, dayOfWeek)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        if (isViewAttached()) {
                            view.showAddedToPlanMessage(dayOfWeek);
                        }
                    },
                    throwable -> {
                        if (isViewAttached()) {
                            view.showError("Failed to add to plan");
                        }
                    }
                )
        );
    }
}

