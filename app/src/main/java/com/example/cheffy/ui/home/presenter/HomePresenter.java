package com.example.cheffy.ui.home.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.data.meals.models.Area;
import com.example.cheffy.data.meals.models.Category;
import com.example.cheffy.data.meals.models.Ingredient;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.repository.IMealsRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    private final IMealsRepository homeRepository;
    private final IAuthRepository authRepository;

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
