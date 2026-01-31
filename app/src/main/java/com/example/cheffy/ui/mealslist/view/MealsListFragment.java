package com.example.cheffy.ui.mealslist.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cheffy.R;
import com.example.cheffy.common.base.BaseFragment;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.data.meals.repository.MealsRepositoryImpl;
import com.example.cheffy.ui.mealslist.presenter.MealsListContract;
import com.example.cheffy.ui.mealslist.presenter.MealsListPresenter;
import com.example.cheffy.ui.mealslist.view.adapter.MealsGridAdapter;
import com.example.cheffy.ui.mealslist.view.adapter.OnMealGridClickListener;

import java.util.List;
import java.util.Set;

public class MealsListFragment extends BaseFragment implements
        MealsListContract.View,
        OnMealGridClickListener {

    private TextView tvTitle, tvSubtitle;
    private ImageButton btnBack;
    private RecyclerView rvMeals;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    private MealsGridAdapter adapter;
    private MealsListContract.Presenter presenter;

    private String filter;
    private SearchType searchType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            MealsListFragmentArgs args = MealsListFragmentArgs.fromBundle(getArguments());
            filter = args.getFilter();
            searchType = args.getSearchType();
        }

        presenter = new MealsListPresenter(MealsRepositoryImpl.getInstance(requireContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meals_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupAdapter();
        setupListeners();

        presenter.attachView(this);
        presenter.loadMeals(filter, searchType);
    }

    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.tvListTitle);
        tvSubtitle = view.findViewById(R.id.tvListSubtitle);
        btnBack = view.findViewById(R.id.btnBack);
        rvMeals = view.findViewById(R.id.rvMeals);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
    }

    private void setupAdapter() {
        adapter = new MealsGridAdapter(this);
        rvMeals.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            if (isAdded() && getView() != null) {
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        swipeRefresh.setOnRefreshListener(() -> {
            presenter.loadMeals(filter, searchType);
        });

        swipeRefresh.setColorSchemeResources(R.color.primary_color);
    }


    @Override
    public void showLoading() {
        if (!isAdded() || getView() == null) return;

        hideErrorState(new View[]{swipeRefresh});
        progressBar.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        if (!isAdded() || getView() == null) return;

        progressBar.setVisibility(View.GONE);
        swipeRefresh.setRefreshing(false);
        swipeRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMeals(List<RemoteMeal> meals) {
        if (!isAdded() || getView() == null) return;

        adapter.setMeals(meals);
    }

    @Override
    public void showError(String message) {
        if (!isAdded() || getView() == null) return;

        hideLoading();
        showErrorState(
                R.id.stubError,
                message,
                new View[]{swipeRefresh},
                () -> presenter.onTryAgainClicked()
        );
    }

    @Override
    public void showEmpty() {
        if (!isAdded() || getView() == null) return;

        showErrorState(
                R.id.stubError,
                "No meals found",
                new View[]{swipeRefresh},
                () -> presenter.onTryAgainClicked()
        );
    }

    @Override
    public void navigateToMealDetails(RemoteMeal meal) {
        if (!isAdded() || getView() == null || meal == null) return;

        MealsListFragmentDirections.ActionMealsListFragmentToMealDetailsFragment action =
                MealsListFragmentDirections.actionMealsListFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void setTitle(String title) {
        if (!isAdded() || getView() == null) return;

        tvTitle.setText(title);
    }

    @Override
    public void setSubtitle(String subtitle) {
        if (!isAdded() || getView() == null) return;

        tvSubtitle.setText(subtitle);
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
    public void showAddedToFavorites(String mealId) {
        if (!isAdded() || getView() == null) return;
        adapter.updateFavoriteStatus(mealId, true);
        showSnackBarSuccess("Added to favorites!");
    }

    @Override
    public void showRemovedFromFavorites(String mealId) {
        if (!isAdded() || getView() == null) return;
        adapter.updateFavoriteStatus(mealId, false);
        showSnackBarSuccess("Removed from favorites!");
    }

    @Override
    public void setFavoriteIds(Set<String> favoriteIds) {
        if (!isAdded() || getView() == null) return;
        adapter.setFavoriteIds(favoriteIds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}

