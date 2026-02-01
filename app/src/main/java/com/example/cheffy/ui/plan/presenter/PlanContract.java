package com.example.cheffy.ui.plan.presenter;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.ui.plan.model.DayItem;
import com.example.cheffy.ui.plan.model.PlannedMeal;

import java.util.List;

public interface PlanContract {

    interface View {
        void showDays(List<DayItem> days);
        void renderMeals(List<PlannedMeal> meals);
        void showEmptyState();
        void hideEmptyState();
        void updateSelectedDay(String dayName);
        void showError(String message);
        void showUndoSnackbar(PlannedMeal plannedMeal);
        void navigateToMealDetails(RemoteMeal meal);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void onDaySelected(String dayOfWeek);
        void onMealClicked(PlannedMeal plannedMeal);
        void onRemoveMealClicked(PlannedMeal plannedMeal);
        void onUndoRemove(PlannedMeal plannedMeal);
    }
}
