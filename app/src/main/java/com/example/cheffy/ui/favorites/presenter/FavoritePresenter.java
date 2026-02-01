package com.example.cheffy.ui.favorites.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.ui.favorites.FavoriteFilterType;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class FavoritePresenter extends BasePresenter<FavoriteContract.View> 
        implements FavoriteContract.Presenter {

    private final IMealsRepository mealsRepository;

    private final BehaviorSubject<FavoriteFilterType> filterSubject = BehaviorSubject.createDefault(FavoriteFilterType.ALL);
    private final BehaviorSubject<String> searchSubject = BehaviorSubject.createDefault("");

    public FavoritePresenter(IMealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @Override
    public void attachView(FavoriteContract.View view) {
        super.attachView(view);
        subscribeToFavorites();
    }

    private void subscribeToFavorites() {
        addDisposable(
            Flowable.combineLatest(
                mealsRepository.observeFavorites(),
                filterSubject.toFlowable(BackpressureStrategy.LATEST),
                searchSubject.toFlowable(BackpressureStrategy.LATEST),
                this::applyFilters
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                meals -> {
                    if (isViewAttached()) {
                        if (meals.isEmpty()) {
                            view.showEmptyState();
                        } else {
                            view.hideEmptyState();
                            view.renderFavorites(meals);
                        }
                    }
                },
                error -> {
                    if (isViewAttached()) {
                        view.showError(error.getMessage() != null ? error.getMessage() : "Failed to load favorites");
                    }
                }
            )
        );
    }

    private List<RemoteMeal> applyFilters(List<RemoteMeal> meals, FavoriteFilterType filterType, String searchQuery) {
        List<RemoteMeal> result = applyFilter(meals, filterType);
        return applySearch(result, searchQuery);
    }

    private List<RemoteMeal> applySearch(List<RemoteMeal> meals, String query) {
        if (query == null || query.trim().isEmpty()) {
            return meals;
        }
        
        String lowerQuery = query.toLowerCase().trim();
        List<RemoteMeal> filtered = new ArrayList<>();
        for (RemoteMeal meal : meals) {
            String name = meal.getName();
            if (name != null && name.toLowerCase().startsWith(lowerQuery)) {
                filtered.add(meal);
            }
        }
        return filtered;
    }

    private List<RemoteMeal> applyFilter(List<RemoteMeal> meals, FavoriteFilterType filterType) {
        if (filterType == FavoriteFilterType.ALL) {
            return meals;
        }

        List<RemoteMeal> filtered = new ArrayList<>();
        for (RemoteMeal meal : meals) {
            String category = meal.getCategory();
            
            if (filterType == FavoriteFilterType.DESSERT) {
                if (category != null && category.equalsIgnoreCase("Dessert")) {
                    filtered.add(meal);
                }
            } else if (filterType == FavoriteFilterType.FOOD) {
                if (category == null || !category.equalsIgnoreCase("Dessert")) {
                    filtered.add(meal);
                }
            }
        }
        return filtered;
    }

    @Override
    public void onMealClicked(RemoteMeal meal) {
        if (isViewAttached() && meal != null) {
            view.navigateToMealDetails(meal);
        }
    }

    @Override
    public void onRemoveFavorite(RemoteMeal meal) {
        if (meal == null) return;
        
        addDisposable(
            mealsRepository.removeFavorite(meal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        if (isViewAttached()) {
                            view.showUndoSnackbar(meal);
                        }
                    },
                    error -> {
                        if (isViewAttached()) {
                            view.showError("Failed to remove from favorites");
                        }
                    }
                )
        );
    }

    @Override
    public void onUndoRemove(RemoteMeal meal) {
        if (meal == null) return;
        
        addDisposable(
            mealsRepository.addFavorite(meal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> { },
                    error -> {
                        if (isViewAttached()) {
                            view.showError("Failed to restore favorite");
                        }
                    }
                )
        );
    }

    @Override
    public void onFilterSelected(FavoriteFilterType filterType) {
        filterSubject.onNext(filterType);
    }

    @Override
    public void onSearchQueryChanged(String query) {
        searchSubject.onNext(query != null ? query : "");
    }
}
