package com.example.cheffy.ui.search.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.ui.search.SearchFilterType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class SearchPresenter extends BasePresenter<SearchContract.View>
        implements SearchContract.Presenter {

    private static final String DEFAULT_FIRST_LETTER = "c";
    private static final long DEBOUNCE_TIMEOUT_MS = 300;
    private final IMealsRepository repository;
    private final BehaviorSubject<SearchFilterType> activeChipSubject = BehaviorSubject.createDefault(SearchFilterType.MEAL_NAME);
    private final BehaviorSubject<String> querySubject = BehaviorSubject.createDefault("");
    private List<Category> cachedCategories = new ArrayList<>();
    private List<Area> cachedAreas = new ArrayList<>();
    private List<Ingredient> cachedIngredients = new ArrayList<>();
    private Set<String> favoriteIds = new HashSet<>();
    private boolean categoriesLoaded = false;
    private boolean areasLoaded = false;
    private boolean ingredientsLoaded = false;

    public SearchPresenter(IMealsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(SearchContract.View view) {
        super.attachView(view);
        setupSearchSubscription();
        loadFavoriteIds();
    }

    private void loadFavoriteIds() {
        addDisposable(
            repository.observeFavorites()
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
    public void loadInitialData(SearchFilterType initialFilter) {
        if (!isViewAttached()) return;

        activeChipSubject.onNext(initialFilter);
        view.selectChip(initialFilter);

        switch (initialFilter) {
            case MEAL_NAME:
                loadDiscoverMeals();
                break;
            case CATEGORY:
                loadCategories();
                break;
            case COUNTRY:
                loadAreas();
                break;
            case INGREDIENT:
                loadIngredients();
                break;
        }
    }

    @Override
    public void onChipSelected(SearchFilterType type) {
        if (!isViewAttached()) return;

        activeChipSubject.onNext(type);

        querySubject.onNext("");

        switch (type) {
            case MEAL_NAME:
                loadDiscoverMeals();
                break;
            case CATEGORY:
                if (categoriesLoaded) {
                    view.renderCategories(cachedCategories);
                } else {
                    loadCategories();
                }
                break;
            case COUNTRY:
                if (areasLoaded) {
                    view.renderAreas(cachedAreas);
                } else {
                    loadAreas();
                }
                break;
            case INGREDIENT:
                if (ingredientsLoaded) {
                    view.renderIngredients(cachedIngredients);
                } else {
                    loadIngredients();
                }
                break;
        }
    }

    @Override
    public void onSearchQueryChanged(String query) {
        querySubject.onNext(query != null ? query : "");
    }

    @Override
    public void onMealClicked(RemoteMeal meal) {
        if (isViewAttached()) {
            view.navigateToMealDetails(meal);
        }
    }

    @Override
    public void onCategoryClicked(Category category) {
        if (isViewAttached()) {
            view.navigateToMealsList(category.getName(), SearchType.CATEGORY);
        }
    }

    @Override
    public void onAreaClicked(Area area) {
        if (isViewAttached()) {
            view.navigateToMealsList(area.getName(), SearchType.AREA);
        }
    }

    @Override
    public void onIngredientClicked(Ingredient ingredient) {
        if (isViewAttached()) {
            view.navigateToMealsList(ingredient.getName(), SearchType.INGREDIENT);
        }
    }

    @Override
    public void onMealFavoriteClicked(RemoteMeal meal) {
        if (!isViewAttached() || meal == null) return;
        
        String mealId = meal.getIdMeal();
        boolean isFavorite = favoriteIds.contains(mealId);
        
        if (isFavorite) {
            addDisposable(
                repository.removeFavorite(meal)
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
                repository.addFavorite(meal)
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

    private void setupSearchSubscription() {
        Disposable disposable = Observable.combineLatest(
                activeChipSubject.distinctUntilChanged(),
                querySubject.debounce(DEBOUNCE_TIMEOUT_MS, TimeUnit.MILLISECONDS).distinctUntilChanged(),
                SearchState::new
        )
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSearchState, this::handleError);

        addDisposable(disposable);
    }

    private void handleSearchState(SearchState state) {
        if (!isViewAttached()) return;

        String query = state.query.trim();

        switch (state.filterType) {
            case MEAL_NAME:
                if (query.isEmpty()) {
                    loadDiscoverMeals();
                } else {
                    searchMealsRemotely(query);
                }
                break;
            case CATEGORY:
                filterCategoriesLocally(query);
                break;
            case COUNTRY:
                filterAreasLocally(query);
                break;
            case INGREDIENT:
                filterIngredientsLocally(query);
                break;
        }
    }

    private void handleError(Throwable throwable) {
        if (isViewAttached()) {
            view.hideLoading();
            view.showError(throwable.getMessage() != null ? throwable.getMessage() : "An error occurred");
        }
    }

    private void loadDiscoverMeals() {
        if (!isViewAttached()) return;

        view.showLoading();
        view.hideError();

        Disposable disposable = repository.searchMealsByFirstLetter(DEFAULT_FIRST_LETTER)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (isViewAttached()) {
                                view.hideLoading();
                                if (meals.isEmpty()) {
                                    view.showEmptyState("No meals found");
                                } else {
                                    view.renderMeals(meals);
                                }
                            }
                        },
                        this::handleError
                );

        addDisposable(disposable);
    }

    private void searchMealsRemotely(String query) {
        if (!isViewAttached()) return;

        view.showLoading();
        view.hideError();

        Disposable disposable = repository.searchMealsByName(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (isViewAttached()) {
                                view.hideLoading();
                                if (meals.isEmpty()) {
                                    view.showEmptyState("No meals found for \"" + query + "\"");
                                } else {
                                    view.renderMeals(meals);
                                }
                            }
                        },
                        this::handleError
                );

        addDisposable(disposable);
    }

    private void loadCategories() {
        if (!isViewAttached()) return;

        view.showLoading();
        view.hideError();

        Disposable disposable = repository.getCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> {
                            if (isViewAttached()) {
                                view.hideLoading();
                                cachedCategories = categories;
                                categoriesLoaded = true;
                                if (categories.isEmpty()) {
                                    view.showEmptyState("No categories found");
                                } else {
                                    view.renderCategories(categories);
                                }
                            }
                        },
                        this::handleError
                );

        addDisposable(disposable);
    }

    private void loadAreas() {
        if (!isViewAttached()) return;

        view.showLoading();
        view.hideError();

        Disposable disposable = repository.getAreas()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        areas -> {
                            if (isViewAttached()) {
                                view.hideLoading();
                                cachedAreas = areas;
                                areasLoaded = true;
                                if (areas.isEmpty()) {
                                    view.showEmptyState("No countries found");
                                } else {
                                    view.renderAreas(areas);
                                }
                            }
                        },
                        this::handleError
                );

        addDisposable(disposable);
    }

    private void loadIngredients() {
        if (!isViewAttached()) return;

        view.showLoading();
        view.hideError();

        Disposable disposable = repository.getIngredients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredients -> {
                            if (isViewAttached()) {
                                view.hideLoading();
                                cachedIngredients = ingredients;
                                ingredientsLoaded = true;
                                if (ingredients.isEmpty()) {
                                    view.showEmptyState("No ingredients found");
                                } else {
                                    view.renderIngredients(ingredients);
                                }
                            }
                        },
                        this::handleError
                );

        addDisposable(disposable);
    }

    private void filterCategoriesLocally(String query) {
        if (!isViewAttached()) return;

        if (query.isEmpty()) {
            view.renderCategories(cachedCategories);
            return;
        }

        List<Category> filtered = cachedCategories.stream()
                .filter(c -> c.getName().toLowerCase().startsWith(query.toLowerCase()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            view.showEmptyState("No categories match \"" + query + "\"");
        } else {
            view.renderCategories(filtered);
        }
    }

    private void filterAreasLocally(String query) {
        if (!isViewAttached()) return;

        if (query.isEmpty()) {
            view.renderAreas(cachedAreas);
            return;
        }

        List<Area> filtered = cachedAreas.stream()
                .filter(a -> a.getName().toLowerCase().startsWith(query.toLowerCase()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            view.showEmptyState("No countries match \"" + query + "\"");
        } else {
            view.renderAreas(filtered);
        }
    }

    private void filterIngredientsLocally(String query) {
        if (!isViewAttached()) return;

        if (query.isEmpty()) {
            view.renderIngredients(cachedIngredients);
            return;
        }

        List<Ingredient> filtered = cachedIngredients.stream()
                .filter(i -> i.getName().toLowerCase().startsWith(query.toLowerCase()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            view.showEmptyState("No ingredients match \"" + query + "\"");
        } else {
            view.renderIngredients(filtered);
        }
    }

    private static class SearchState {
        final SearchFilterType filterType;
        final String query;

        SearchState(SearchFilterType filterType, String query) {
            this.filterType = filterType;
            this.query = query;
        }
    }
}

