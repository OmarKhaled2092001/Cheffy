package com.example.cheffy.ui.mealslist.presenter;

import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.data.meals.repository.MealsDataCallback;

import java.util.List;

public class MealsListPresenter implements MealsListContract.Presenter {

    private MealsListContract.View view;
    private final IMealsRepository mealsRepository;

    private String currentFilter;
    private SearchType currentType;

    public MealsListPresenter(IMealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @Override
    public void attachView(MealsListContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadMeals(String filter, SearchType type) {
        if (view == null) return;

        this.currentFilter = filter;
        this.currentType = type;

        view.setTitle(type.getDisplayTitle(filter));
        view.setSubtitle(type.getSubtitle());

        view.showLoading();

        mealsRepository.getMealsByFilter(type, filter, new MealsDataCallback<List<RemoteMeal>>() {
            @Override
            public void onSuccess(List<RemoteMeal> data) {
                if (view == null) return;

                view.hideLoading();

                if (data == null || data.isEmpty()) {
                    view.showEmpty();
                } else {
                    view.showMeals(data);
                }
            }

            @Override
            public void onError(String message) {
                if (view == null) return;

                view.hideLoading();
                view.showError(message);
            }

        });
    }

    @Override
    public void onMealClicked(RemoteMeal meal) {
        if (view != null && meal != null) {
            view.navigateToMealDetails(meal);
        }
    }

    @Override
    public void onTryAgainClicked() {
        if (currentFilter != null && currentType != null) {
            loadMeals(currentFilter, currentType);
        }
    }
}
