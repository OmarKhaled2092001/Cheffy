package com.example.cheffy.ui.home.presenter;

import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.data.meals.models.Area;
import com.example.cheffy.data.meals.models.Category;
import com.example.cheffy.data.meals.models.Ingredient;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.repository.MealsDataCallback;
import com.example.cheffy.data.meals.repository.IMealsRepository;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private final IMealsRepository homeRepository;
    private final IAuthRepository authRepository;

    private static final int POPULAR_MEALS_COUNT = 10;

    public HomePresenter(IMealsRepository homeRepository, IAuthRepository authRepository) {
        this.homeRepository = homeRepository;
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadHomeData() {
        if (view == null) return;

        loadUserName();

        view.showLoading();

        loadMealOfTheDay();
        loadCategories();
        loadPopularMeals();
        loadCuisines();
        loadIngredients();
    }

    private void loadUserName() {
        if (view == null) return;

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

    private void loadMealOfTheDay() {
        homeRepository.getRandomMeal(new MealsDataCallback<RemoteMeal>() {
            @Override
            public void onSuccess(RemoteMeal data) {
                if (view != null) {
                    view.showMealOfTheDay(data);
                    view.hideLoading();
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                    view.hideLoading();
                }
            }
        });
    }

    private void loadCategories() {
        homeRepository.getCategories(new MealsDataCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> data) {
                if (view != null) {
                    view.showCategories(data);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                }
            }
        });
    }

    private void loadPopularMeals() {
        homeRepository.getPopularMeals(POPULAR_MEALS_COUNT, new MealsDataCallback<List<RemoteMeal>>() {
            @Override
            public void onSuccess(List<RemoteMeal> data) {
                if (view != null) {
                    view.showPopularMeals(data);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                }
            }
        });
    }

    private void loadCuisines() {
        homeRepository.getAreas(new MealsDataCallback<List<Area>>() {
            @Override
            public void onSuccess(List<Area> data) {
                if (view != null) {
                    view.showCuisines(data);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                }
            }
        });
    }

    private void loadIngredients() {
        homeRepository.getIngredients(new MealsDataCallback<List<Ingredient>>() {
            @Override
            public void onSuccess(List<Ingredient> data) {
                if (view != null) {
                    view.showIngredients(data);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                }
            }
        });
    }

    @Override
    public void onMealOfTheDayClicked(RemoteMeal meal) {
        if (view != null && meal != null) {
            view.navigateToMealDetails(meal);
        }
    }

    @Override
    public void onCategoryClicked(String category) {
        if (view != null && category != null) {
            view.navigateToMealsList(category, SearchType.CATEGORY);
        }
    }

    @Override
    public void onCuisineClicked(String area) {
        if (view != null && area != null) {
            view.navigateToMealsList(area, SearchType.AREA);
        }
    }

    @Override
    public void onIngredientClicked(String ingredient) {
        if (view != null && ingredient != null) {
            view.navigateToMealsList(ingredient, SearchType.INGREDIENT);
        }
    }

    @Override
    public void onPopularMealClicked(RemoteMeal meal) {
        if (view != null && meal != null) {
            view.navigateToMealDetails(meal);
        }
    }

    @Override
    public void onTryAgainClicked() {
        loadHomeData();
    }
}
