package com.example.cheffy.ui.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.common.base.BaseFragment;
import com.example.cheffy.data.auth.repository.AuthRepositoryImpl;
import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.repository.MealsRepositoryImpl;
import com.example.cheffy.ui.home.presenter.HomeContract;
import com.example.cheffy.ui.search.SearchFilterType;
import com.example.cheffy.ui.home.presenter.HomePresenter;
import com.example.cheffy.ui.home.view.adapters.CategoriesAdapter;
import com.example.cheffy.ui.home.view.adapters.CuisinesAdapter;
import com.example.cheffy.ui.home.view.adapters.IngredientsAdapter;
import com.example.cheffy.ui.home.view.adapters.OnCategoryClickListener;
import com.example.cheffy.ui.home.view.adapters.OnCuisineClickListener;
import com.example.cheffy.ui.home.view.adapters.OnIngredientClickListener;
import com.example.cheffy.ui.home.view.adapters.OnMealClickListener;
import com.example.cheffy.ui.home.view.adapters.PopularMealsAdapter;

import java.util.List;
import java.util.Set;

public class HomeFragment extends BaseFragment implements HomeContract.View,
        OnCategoryClickListener,
        OnMealClickListener,
        OnCuisineClickListener,
        OnIngredientClickListener {

    private TextView tvUsername, tvMealDayName;
    private CardView cardMealOfTheDay;
    private ImageView imgMealOfTheDay;
    private RecyclerView rvCategories, recyclerViewPopular, rvCuisines, rvIngredients;
    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;
    private TextView tvSeeAllCategories, tvSeeAllCuisines, tvSeeAllIngredients;

    private CategoriesAdapter categoriesAdapter;
    private PopularMealsAdapter popularMealsAdapter;
    private CuisinesAdapter cuisinesAdapter;
    private IngredientsAdapter ingredientsAdapter;

    private RemoteMeal mealOfTheDay;
    private HomeContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new HomePresenter(
                MealsRepositoryImpl.getInstance(requireContext()),
                AuthRepositoryImpl.getInstance()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initAdapters();
        setupClickListeners();

        presenter.attachView(this);
        presenter.loadHomeData();
    }

    private void initViews(View view) {
        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        tvUsername = view.findViewById(R.id.tvUsername);

        cardMealOfTheDay = view.findViewById(R.id.cardMealOfTheDay);
        imgMealOfTheDay = view.findViewById(R.id.imgMealOfTheDay);
        tvMealDayName = view.findViewById(R.id.tvMealDayName);

        rvCategories = view.findViewById(R.id.rvCategories);
        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);
        rvCuisines = view.findViewById(R.id.rvCuisines);
        rvIngredients = view.findViewById(R.id.rvIngredients);

        progressBar = view.findViewById(R.id.progressBar);

        tvSeeAllCategories = view.findViewById(R.id.tvSeeAllCategories);
        tvSeeAllCuisines = view.findViewById(R.id.tvSeeAllCuisines);
        tvSeeAllIngredients = view.findViewById(R.id.tvSeeAllIngredients);
    }

    private void initAdapters() {
        categoriesAdapter = new CategoriesAdapter(this);
        rvCategories.setAdapter(categoriesAdapter);

        popularMealsAdapter = new PopularMealsAdapter(this);
        recyclerViewPopular.setAdapter(popularMealsAdapter);

        cuisinesAdapter = new CuisinesAdapter(this);
        rvCuisines.setAdapter(cuisinesAdapter);

        ingredientsAdapter = new IngredientsAdapter(this);
        rvIngredients.setAdapter(ingredientsAdapter);
    }

    private void setupClickListeners() {
        cardMealOfTheDay.setOnClickListener(v -> {
            if (mealOfTheDay != null) {
                presenter.onMealOfTheDayClicked(mealOfTheDay);
            }
        });

        tvSeeAllCategories.setOnClickListener(v -> navigateToSearch(SearchFilterType.CATEGORY));
        tvSeeAllCuisines.setOnClickListener(v -> navigateToSearch(SearchFilterType.COUNTRY));
        tvSeeAllIngredients.setOnClickListener(v -> navigateToSearch(SearchFilterType.INGREDIENT));
    }

    @Override
    public void showLoading() {
        hideErrorState(new View[]{nestedScrollView});
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (nestedScrollView != null) {
            nestedScrollView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (nestedScrollView != null) {
            nestedScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMealOfTheDay(RemoteMeal meal) {
        if (!isAdded() || getView() == null) return;

        mealOfTheDay = meal;
        tvMealDayName.setText(meal.getName());

        Glide.with(this)
                .load(meal.getThumbnail())
                .centerCrop()
                .placeholder(R.drawable.welcome_background)
                .into(imgMealOfTheDay);
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (!isAdded() || getView() == null) return;
        categoriesAdapter.setCategories(categories);
    }

    @Override
    public void showPopularMeals(List<RemoteMeal> meals) {
        if (!isAdded() || getView() == null) return;
        popularMealsAdapter.setMeals(meals);
    }

    @Override
    public void showCuisines(List<Area> areas) {
        if (!isAdded() || getView() == null) return;
        cuisinesAdapter.setAreas(areas);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        if (!isAdded() || getView() == null) return;
        ingredientsAdapter.setIngredients(ingredients);
    }

    @Override
    public void showError(String message) {
        hideLoading();
        showErrorState(
                R.id.stubError,
                message,
                new View[]{nestedScrollView},
                () -> presenter.onTryAgainClicked()
        );
    }

    @Override
    public void navigateToMealDetails(RemoteMeal meal) {
        if (!isAdded() || getView() == null || meal == null) return;

        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void navigateToMealsList(String filter, SearchType type) {
        if (!isAdded() || getView() == null) return;

        HomeFragmentDirections.ActionHomeFragmentToMealsListFragment action =
                HomeFragmentDirections.actionHomeFragmentToMealsListFragment(filter, type);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void navigateToSearch(SearchFilterType filterType) {
        if (!isAdded() || getView() == null) return;

        HomeFragmentDirections.ActionHomeFragmentToSearchFragment action =
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(filterType);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void setUserName(String name) {
        if (!isAdded() || getView() == null) return;
        tvUsername.setText(name);
    }

    @Override
    public void onCategoryClick(Category category) {
        presenter.onCategoryClicked(category.getName());
    }

    @Override
    public void onMealClick(RemoteMeal meal) {
        presenter.onPopularMealClicked(meal);
    }

    @Override
    public void onFavoriteClick(RemoteMeal meal) {
        presenter.onPopularMealFavoriteClicked(meal);
    }

    @Override
    public void onCuisineClick(Area area) {
        presenter.onCuisineClicked(area.getName());
    }

    @Override
    public void onIngredientClick(Ingredient ingredient) {
        presenter.onIngredientClicked(ingredient.getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void showAddedToFavorites(String mealId) {
        if (!isAdded() || getView() == null) return;
        popularMealsAdapter.updateFavoriteStatus(mealId, true);
        showSnackBarSuccess("Added to favorites!");
    }

    @Override
    public void showRemovedFromFavorites(String mealId) {
        if (!isAdded() || getView() == null) return;
        popularMealsAdapter.updateFavoriteStatus(mealId, false);
        showSnackBarSuccess("Removed from favorites!");
    }

    @Override
    public void setFavoriteIds(Set<String> favoriteIds) {
        if (!isAdded() || getView() == null) return;
        popularMealsAdapter.setFavoriteIds(favoriteIds);
    }
}