package com.example.cheffy.ui.home.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.repository.IMealsRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    private final IMealsRepository homeRepository;
    private final IAuthRepository authRepository;
    private Set<String> favoriteIds = new HashSet<>();

    private static final int POPULAR_MEALS_COUNT = 10;

    public HomePresenter(IMealsRepository homeRepository, IAuthRepository authRepository) {
        this.homeRepository = homeRepository;
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(HomeContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loadHomeData() {
        if (!isViewAttached()) return;

        loadUserName();
        loadFavoriteIds();
        view.showLoading();

        addDisposable(
            Single.zip(
                homeRepository.getRandomMeal(),
                homeRepository.getCategories(),
                homeRepository.getPopularMeals(POPULAR_MEALS_COUNT),
                homeRepository.getAreas(),
                homeRepository.getIngredients(),
                HomeDataBundle::new
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                bundle -> {
                    if (isViewAttached()) {
                        view.hideLoading();
                        view.showMealOfTheDay(bundle.mealOfTheDay);
                        view.showCategories(bundle.categories);
                        view.showPopularMeals(bundle.popularMeals);
                        view.showCuisines(bundle.areas);
                        view.showIngredients(bundle.ingredients);
                    }
                },
                throwable -> {
                    if (isViewAttached()) {
                        view.hideLoading();
                        view.showError(throwable.getMessage() != null 
                            ? throwable.getMessage() 
                            : "Failed to load home data");
                    }
                }
            )
        );
    }

    private void loadFavoriteIds() {
        addDisposable(
            homeRepository.observeFavorites()
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

    private void loadUserName() {
        if (!isViewAttached()) return;

        if (authRepository.isUserLoggedIn() && authRepository.getCurrentUser() != null) {
            String fullName = authRepository.getCurrentUser().getFullName();
            String firstName = fullName != null && fullName.contains(" ")
                    ? fullName.split(" ")[0]
                    : fullName;
            view.setUserName(firstName != null && !firstName.isEmpty() ? firstName : "Guest");
        } else {
            view.setUserName("Guest");
        }
    }

    @Override
    public void onMealOfTheDayClicked(RemoteMeal meal) {
        if (isViewAttached() && meal != null) {
            view.navigateToMealDetails(meal);
        }
    }

    @Override
    public void onCategoryClicked(String category) {
        if (isViewAttached() && category != null) {
            view.navigateToMealsList(category, SearchType.CATEGORY);
        }
    }

    @Override
    public void onCuisineClicked(String area) {
        if (isViewAttached() && area != null) {
            view.navigateToMealsList(area, SearchType.AREA);
        }
    }

    @Override
    public void onIngredientClicked(String ingredient) {
        if (isViewAttached() && ingredient != null) {
            view.navigateToMealsList(ingredient, SearchType.INGREDIENT);
        }
    }

    @Override
    public void onPopularMealClicked(RemoteMeal meal) {
        if (isViewAttached() && meal != null) {
            view.navigateToMealDetails(meal);
        }
    }

    @Override
    public void onTryAgainClicked() {
        loadHomeData();
    }

    @Override
    public void onPopularMealFavoriteClicked(RemoteMeal meal) {
        if (!isViewAttached() || meal == null) return;
        
        String mealId = meal.getIdMeal();
        boolean isFavorite = favoriteIds.contains(mealId);
        
        if (isFavorite) {
            // Remove from favorites
            addDisposable(
                homeRepository.removeFavorite(meal)
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
            // Add to favorites
            addDisposable(
                homeRepository.addFavorite(meal)
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

    private static class HomeDataBundle {
        final RemoteMeal mealOfTheDay;
        final List<Category> categories;
        final List<RemoteMeal> popularMeals;
        final List<Area> areas;
        final List<Ingredient> ingredients;

        HomeDataBundle(RemoteMeal mealOfTheDay, 
                       List<Category> categories,
                       List<RemoteMeal> popularMeals, 
                       List<Area> areas,
                       List<Ingredient> ingredients) {
            this.mealOfTheDay = mealOfTheDay;
            this.categories = categories;
            this.popularMeals = popularMeals;
            this.areas = areas;
            this.ingredients = ingredients;
        }
    }
}

