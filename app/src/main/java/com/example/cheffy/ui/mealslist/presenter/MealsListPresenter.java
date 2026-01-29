package com.example.cheffy.ui.mealslist.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.repository.IMealsRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MealsListPresenter extends BasePresenter<MealsListContract.View> implements MealsListContract.Presenter {

    private final IMealsRepository mealsRepository;

    private String currentFilter;
    private SearchType currentType;

    public MealsListPresenter(IMealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @Override
    public void attachView(MealsListContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loadMeals(String filter, SearchType type) {
        if (!isViewAttached()) return;

        this.currentFilter = filter;
        this.currentType = type;

        view.setTitle(type.getDisplayTitle(filter));
        view.setSubtitle(type.getSubtitle());

        view.showLoading();

        addDisposable(
            mealsRepository.getMealsByFilter(type, filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    meals -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            if (meals == null || meals.isEmpty()) {
                                view.showEmpty();
                            } else {
                                view.showMeals(meals);
                            }
                        }
                    },
                    throwable -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showError(throwable.getMessage() != null 
                                ? throwable.getMessage() 
                                : "Failed to load meals");
                        }
                    }
                )
        );
    }

    @Override
    public void onMealClicked(RemoteMeal meal) {
        if (isViewAttached() && meal != null) {
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
