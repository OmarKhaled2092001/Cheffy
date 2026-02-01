package com.example.cheffy.ui.mealslist.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.repository.IMealsRepository;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MealsListPresenter extends BasePresenter<MealsListContract.View> implements MealsListContract.Presenter {

    private final IMealsRepository mealsRepository;
    private final Set<String> favoriteIds = new HashSet<>();

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

        loadFavoriteIds();
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

    private void loadFavoriteIds() {
        addDisposable(
            mealsRepository.observeFavorites()
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    favorites -> {
                        favoriteIds.clear();
                        for (RemoteMeal meal : favorites) {
                            favoriteIds.add(meal.getIdMeal());
                        }
                        if (isViewAttached()) {
                            view.setFavoriteIds(favoriteIds);
                        }
                    },
                    throwable -> { }
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

    @Override
    public void onMealFavoriteClicked(RemoteMeal meal) {
        if (!isViewAttached() || meal == null) return;
        
        String mealId = meal.getIdMeal();
        boolean isFavorite = favoriteIds.contains(mealId);
        
        if (isFavorite) {
            addDisposable(
                mealsRepository.removeFavorite(meal)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        () -> {
                            favoriteIds.remove(mealId);
                            if (isViewAttached()) {
                                view.showRemovedFromFavorites(mealId);
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
                mealsRepository.addFavorite(meal)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        () -> {
                            favoriteIds.add(mealId);
                            if (isViewAttached()) {
                                view.showAddedToFavorites(mealId);
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
}

