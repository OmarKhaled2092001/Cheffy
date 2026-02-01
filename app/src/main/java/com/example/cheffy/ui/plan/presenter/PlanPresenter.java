package com.example.cheffy.ui.plan.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.ui.plan.model.DayItem;
import com.example.cheffy.ui.plan.model.PlannedMeal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class PlanPresenter extends BasePresenter<PlanContract.View>
        implements PlanContract.Presenter {

    private final IMealsRepository mealsRepository;
    private static final String[] DAYS_OF_WEEK = {
            "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    };

    private static final String[] DAYS_SHORT = {
            "SAT", "SUN", "MON", "TUE", "WED", "THU", "FRI"
    };
    private final BehaviorSubject<String> daySubject;

    public PlanPresenter(IMealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
        this.daySubject = BehaviorSubject.createDefault(getCurrentDayName());
    }

    private String getCurrentDayName() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY: return "Sunday";
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            default: return "Saturday";
        }
    }

    @Override
    public void attachView(PlanContract.View view) {
        super.attachView(view);
        initializeDays();
        subscribeToMealPlan();
    }

    private void initializeDays() {
        List<DayItem> days = generateWeekDays();
        if (isViewAttached()) {
            view.showDays(days);
        }
    }

    private List<DayItem> generateWeekDays() {
        List<DayItem> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int todayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        int todayIndex = calendarDayToIndex(todayDayOfWeek);

        String currentSelection = daySubject.getValue();

        for (int i = 0; i < 7; i++) {
            int dayIndex = (todayIndex + i) % 7;
            DayItem day = new DayItem(
                    DAYS_OF_WEEK[dayIndex],
                    DAYS_SHORT[dayIndex],
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            
            if (DAYS_OF_WEEK[dayIndex].equals(currentSelection)) {
                day.setSelected(true);
            }
            
            days.add(day);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }

    private int calendarDayToIndex(int calendarDay) {
        switch (calendarDay) {
            case Calendar.SUNDAY: return 1;
            case Calendar.MONDAY: return 2;
            case Calendar.TUESDAY: return 3;
            case Calendar.WEDNESDAY: return 4;
            case Calendar.THURSDAY: return 5;
            case Calendar.FRIDAY: return 6;
            default: return 0;
        }
    }

    private void subscribeToMealPlan() {
        addDisposable(
                daySubject
                        .toFlowable(BackpressureStrategy.LATEST)
                        .switchMap(mealsRepository::observeMealPlanByDay)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (isViewAttached()) {
                                        String currentDay = daySubject.getValue();
                                        view.updateSelectedDay(currentDay);

                                        if (meals.isEmpty()) {
                                            view.showEmptyState();
                                        } else {
                                            view.hideEmptyState();
                                            view.renderMeals(meals);
                                        }
                                    }
                                },
                                error -> {
                                    if (isViewAttached()) {
                                        view.showError("Failed to load meal plan");
                                    }
                                }
                        )
        );
    }

    @Override
    public void onDaySelected(String dayOfWeek) {
        daySubject.onNext(dayOfWeek);
    }

    @Override
    public void onMealClicked(PlannedMeal plannedMeal) {
        if (isViewAttached() && plannedMeal != null && plannedMeal.getMeal() != null) {
            view.navigateToMealDetails(plannedMeal.getMeal());
        }
    }

    @Override
    public void onRemoveMealClicked(PlannedMeal plannedMeal) {
        if (plannedMeal == null || plannedMeal.getMeal() == null) return;

        addDisposable(
                mealsRepository.removeMealFromPlanWithSync(
                        plannedMeal.getMeal().getIdMeal(),
                        plannedMeal.getDayOfWeek(),
                        plannedMeal.getPlanId()
                )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (isViewAttached()) {
                                        view.showUndoSnackbar(plannedMeal);
                                    }
                                },
                                error -> {
                                    if (isViewAttached()) {
                                        view.showError("Failed to remove meal from plan");
                                    }
                                }
                        )
        );
    }

    @Override
    public void onUndoRemove(PlannedMeal plannedMeal) {
        if (plannedMeal == null || plannedMeal.getMeal() == null) return;

        addDisposable(
                mealsRepository.addMealToPlan(plannedMeal.getMeal(), plannedMeal.getDayOfWeek())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {},
                                error -> {
                                    if (isViewAttached()) {
                                        view.showError("Failed to restore meal");
                                    }
                                }
                        )
        );
    }
}
