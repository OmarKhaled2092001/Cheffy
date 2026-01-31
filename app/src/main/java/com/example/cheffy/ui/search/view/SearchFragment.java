package com.example.cheffy.ui.search.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.repository.MealsRepositoryImpl;
import com.example.cheffy.ui.search.SearchFilterType;
import com.example.cheffy.ui.search.view.adapter.OnAreaClickListener;
import com.example.cheffy.ui.search.view.adapter.OnCategoryClickListener;
import com.example.cheffy.ui.search.view.adapter.OnIngredientClickListener;
import com.example.cheffy.ui.search.view.adapter.OnMealClickListener;
import com.example.cheffy.ui.search.view.adapter.SearchAreasAdapter;
import com.example.cheffy.ui.search.view.adapter.SearchCategoriesAdapter;
import com.example.cheffy.ui.search.view.adapter.SearchIngredientsAdapter;
import com.example.cheffy.ui.search.view.adapter.SearchMealsAdapter;
import com.example.cheffy.ui.search.presenter.SearchContract;
import com.example.cheffy.ui.search.presenter.SearchPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment implements SearchContract.View,
        OnMealClickListener,
        OnCategoryClickListener,
        OnAreaClickListener,
        OnIngredientClickListener {

    private EditText etSearch;
    private Chip chipMealName, chipCategory, chipCountry, chipIngredient;
    private RecyclerView rvSearchResults;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private TextView tvEmptyMessage;
    private ViewStub errorViewStub;
    private View errorStateLayout;

    private SearchMealsAdapter mealsAdapter;
    private SearchCategoriesAdapter categoriesAdapter;
    private SearchAreasAdapter areasAdapter;
    private SearchIngredientsAdapter ingredientsAdapter;

    private SearchContract.Presenter presenter;
    private SearchFilterType currentFilterType = SearchFilterType.MEAL_NAME;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenter(MealsRepositoryImpl.getInstance(requireContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initAdapters();
        setupChipListeners();
        setupSearchListener();

        presenter.attachView(this);

        SearchFilterType initialFilter = getInitialFilterFromArgs();
        presenter.loadInitialData(initialFilter);
    }

    private void initViews(View view) {
        etSearch = view.findViewById(R.id.etSearch);
        chipMealName = view.findViewById(R.id.chipMealName);
        chipCategory = view.findViewById(R.id.chipCategory);
        chipCountry = view.findViewById(R.id.chipCountry);
        chipIngredient = view.findViewById(R.id.chipIngredient);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage);
        errorViewStub = view.findViewById(R.id.errorViewStub);

        rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
    }

    private void initAdapters() {
        mealsAdapter = new SearchMealsAdapter(this);
        categoriesAdapter = new SearchCategoriesAdapter(this);
        areasAdapter = new SearchAreasAdapter(this);
        ingredientsAdapter = new SearchIngredientsAdapter(this);

        rvSearchResults.setAdapter(mealsAdapter);
    }

    private void setupChipListeners() {
        chipMealName.setOnClickListener(v -> {
            currentFilterType = SearchFilterType.MEAL_NAME;
            rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            presenter.onChipSelected(SearchFilterType.MEAL_NAME);
            clearSearchField();
        });

        chipCategory.setOnClickListener(v -> {
            currentFilterType = SearchFilterType.CATEGORY;
            rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            presenter.onChipSelected(SearchFilterType.CATEGORY);
            clearSearchField();
        });

        chipCountry.setOnClickListener(v -> {
            currentFilterType = SearchFilterType.COUNTRY;
            rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            presenter.onChipSelected(SearchFilterType.COUNTRY);
            clearSearchField();
        });

        chipIngredient.setOnClickListener(v -> {
            currentFilterType = SearchFilterType.INGREDIENT;
            rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            presenter.onChipSelected(SearchFilterType.INGREDIENT);
            clearSearchField();
        });
    }

    private void clearSearchField() {
        etSearch.setText("");
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onSearchQueryChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private SearchFilterType getInitialFilterFromArgs() {
        if (getArguments() != null) {
            try {
                SearchFragmentArgs args = SearchFragmentArgs.fromBundle(getArguments());
                return args.getInitialFilter();
            } catch (Exception e) {
                return currentFilterType;
            }
        }
        return SearchFilterType.MEAL_NAME;
    }


    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        if (errorStateLayout != null) {
            errorStateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void renderMeals(List<RemoteMeal> meals) {
        if (rvSearchResults.getAdapter() != mealsAdapter) {
            rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            rvSearchResults.setAdapter(mealsAdapter);
        }
        mealsAdapter.setMeals(meals);
        showContentState();
    }

    @Override
    public void renderCategories(List<Category> categories) {
        if (rvSearchResults.getAdapter() != categoriesAdapter) {
            rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            rvSearchResults.setAdapter(categoriesAdapter);
        }
        categoriesAdapter.setCategories(categories);
        showContentState();
    }

    @Override
    public void renderAreas(List<Area> areas) {
        if (rvSearchResults.getAdapter() != areasAdapter) {
            rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            rvSearchResults.setAdapter(areasAdapter);
        }
        areasAdapter.setAreas(areas);
        showContentState();
    }

    @Override
    public void renderIngredients(List<Ingredient> ingredients) {
        if (rvSearchResults.getAdapter() != ingredientsAdapter) {
            rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            rvSearchResults.setAdapter(ingredientsAdapter);
        }
        ingredientsAdapter.setIngredients(ingredients);
        showContentState();
    }

    private void showContentState() {
        progressBar.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        if (errorStateLayout != null) {
            errorStateLayout.setVisibility(View.GONE);
        }
        rvSearchResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyState(String message) {
        progressBar.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.GONE);
        if (errorStateLayout != null) {
            errorStateLayout.setVisibility(View.GONE);
        }
        emptyStateLayout.setVisibility(View.VISIBLE);
        tvEmptyMessage.setText(message);
    }

    @Override
    public void showError(String message) {
        inflateErrorStateIfNeeded();
        progressBar.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        if (errorStateLayout != null) {
            errorStateLayout.setVisibility(View.VISIBLE);
        }
    }

    private void inflateErrorStateIfNeeded() {
        if (errorStateLayout == null && errorViewStub != null) {
            errorStateLayout = errorViewStub.inflate();
            MaterialButton btnTryAgain = errorStateLayout.findViewById(R.id.btnTryAgain);
            btnTryAgain.setOnClickListener(v -> presenter.onChipSelected(currentFilterType));
        }
    }

    @Override
    public void hideError() {
        if (errorStateLayout != null) {
            errorStateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void selectChip(SearchFilterType type) {
        currentFilterType = type;
        switch (type) {
            case MEAL_NAME:
                chipMealName.setChecked(true);
                rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                break;
            case CATEGORY:
                chipCategory.setChecked(true);
                rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                break;
            case COUNTRY:
                chipCountry.setChecked(true);
                rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                break;
            case INGREDIENT:
                chipIngredient.setChecked(true);
                rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                break;
        }
    }

    @Override
    public void navigateToMealDetails(RemoteMeal meal) {
        SearchFragmentDirections.ActionNavSearchToMealDetailsFragment action =
                SearchFragmentDirections.actionNavSearchToMealDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void navigateToMealsList(String filter, SearchType type) {
        SearchFragmentDirections.ActionNavSearchToMealsListFragment action =
                SearchFragmentDirections.actionNavSearchToMealsListFragment(filter, type);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onMealClick(RemoteMeal meal) {
        presenter.onMealClicked(meal);
    }

    @Override
    public void onFavoriteClick(RemoteMeal meal) {
        presenter.onMealFavoriteClicked(meal);
    }

    @Override
    public void onCategoryClick(Category category) {
        presenter.onCategoryClicked(category);
    }

    @Override
    public void onAreaClick(Area area) {
        presenter.onAreaClicked(area);
    }

    @Override
    public void onIngredientClick(Ingredient ingredient) {
        presenter.onIngredientClicked(ingredient);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void showAddedToFavorites(String mealId) {
        if (!isAdded() || getView() == null) return;
        mealsAdapter.updateFavoriteStatus(mealId, true);
        Snackbar.make(requireView(), "Added to favorites!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRemovedFromFavorites(String mealId) {
        if (!isAdded() || getView() == null) return;
        mealsAdapter.updateFavoriteStatus(mealId, false);
        Snackbar.make(requireView(), "Removed from favorites!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setFavoriteIds(Set<String> favoriteIds) {
        if (!isAdded() || getView() == null) return;
        mealsAdapter.setFavoriteIds(favoriteIds);
    }
}
