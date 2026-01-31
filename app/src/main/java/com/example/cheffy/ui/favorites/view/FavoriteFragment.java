package com.example.cheffy.ui.favorites.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.repository.MealsRepositoryImpl;
import com.example.cheffy.ui.favorites.FavoriteFilterType;
import com.example.cheffy.ui.favorites.presenter.FavoriteContract;
import com.example.cheffy.ui.favorites.presenter.FavoritePresenter;
import com.example.cheffy.ui.favorites.view.adapter.FavoriteMealsAdapter;
import com.example.cheffy.ui.favorites.view.adapter.OnFavoriteInteractionListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment implements 
        FavoriteContract.View,
        OnFavoriteInteractionListener {

    private RecyclerView rvFavorites;
    private LinearLayout emptyStateLayout;
    private Chip chipAll, chipFood, chipDessert;
    private ImageView btnSearchFavs;
    private EditText etSearchLocal;

    private FavoriteMealsAdapter adapter;
    private FavoriteContract.Presenter presenter;

    private boolean isSearchVisible = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FavoritePresenter(MealsRepositoryImpl.getInstance(requireContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupAdapter();
        setupChipListeners();
        setupSwipeToDelete();
        setupSearchListeners();

        presenter.attachView(this);
    }

    private void initViews(View view) {
        rvFavorites = view.findViewById(R.id.rvFavorites);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        chipAll = view.findViewById(R.id.chipAll);
        chipFood = view.findViewById(R.id.chipFood);
        chipDessert = view.findViewById(R.id.chipDessert);
        btnSearchFavs = view.findViewById(R.id.btnSearchFavs);
        etSearchLocal = view.findViewById(R.id.etSearchLocal);
    }

    private void setupAdapter() {
        adapter = new FavoriteMealsAdapter(this);
        rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFavorites.setAdapter(adapter);
    }

    private void setupChipListeners() {
        chipAll.setOnClickListener(v -> presenter.onFilterSelected(FavoriteFilterType.ALL));
        chipFood.setOnClickListener(v -> presenter.onFilterSelected(FavoriteFilterType.FOOD));
        chipDessert.setOnClickListener(v -> presenter.onFilterSelected(FavoriteFilterType.DESSERT));
    }

    private void setupSearchListeners() {
        btnSearchFavs.setOnClickListener(v -> toggleSearchVisibility());

        etSearchLocal.addTextChangedListener(new TextWatcher() {
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

    private void toggleSearchVisibility() {
        isSearchVisible = !isSearchVisible;
        etSearchLocal.setVisibility(isSearchVisible ? View.VISIBLE : View.GONE);
        if (!isSearchVisible) {
            etSearchLocal.setText("");
            presenter.onSearchQueryChanged("");
        } else {
            etSearchLocal.requestFocus();
        }
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RemoteMeal meal = adapter.getCurrentList().get(position);
                    presenter.onRemoveFavorite(meal);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(rvFavorites);
    }

    @Override
    public void renderFavorites(List<RemoteMeal> meals) {
        if (!isAdded() || getView() == null) return;
        adapter.submitList(new ArrayList<>(meals));
    }

    @Override
    public void showEmptyState() {
        if (!isAdded() || getView() == null) return;
        emptyStateLayout.setVisibility(View.VISIBLE);
        rvFavorites.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyState() {
        if (!isAdded() || getView() == null) return;
        emptyStateLayout.setVisibility(View.GONE);
        rvFavorites.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        if (!isAdded() || getView() == null) return;
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.snack_error_bg, null))
                .setTextColor(getResources().getColor(R.color.white, null))
                .show();
    }

    @Override
    public void showUndoSnackbar(RemoteMeal meal) {
        if (!isAdded() || getView() == null) return;
        
        Snackbar.make(requireView(), R.string.removed_from_favorites, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> presenter.onUndoRemove(meal))
                .setActionTextColor(getResources().getColor(R.color.primary_color, null))
                .show();
    }

    @Override
    public void navigateToMealDetails(RemoteMeal meal) {
        if (!isAdded() || getView() == null || meal == null) return;

        FavoriteFragmentDirections.ActionNavFavoritesToMealDetailsFragment action =
                FavoriteFragmentDirections.actionNavFavoritesToMealDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onMealClick(RemoteMeal meal) {
        presenter.onMealClicked(meal);
    }

    @Override
    public void onDeleteClick(RemoteMeal meal) {
        presenter.onRemoveFavorite(meal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
