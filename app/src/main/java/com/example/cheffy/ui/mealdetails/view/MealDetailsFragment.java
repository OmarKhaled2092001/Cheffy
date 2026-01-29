package com.example.cheffy.ui.mealdetails.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.common.base.BaseFragment;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.ui.mealdetails.model.IngredientItem;
import com.example.cheffy.ui.mealdetails.presenter.MealDetailsContract;
import com.example.cheffy.ui.mealdetails.presenter.MealDetailsPresenter;
import com.example.cheffy.ui.mealdetails.view.adapters.IngredientsListAdapter;
import com.example.cheffy.ui.mealdetails.view.adapters.MealFooterAdapter;
import com.example.cheffy.ui.mealdetails.view.adapters.MealHeaderAdapter;
import com.example.cheffy.ui.mealdetails.view.adapters.MealHeaderListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MealDetailsFragment extends BaseFragment implements MealDetailsContract.View,
        MealHeaderListener {

    private ImageView imgMealDetail;
    private Toolbar toolbar;
    private FloatingActionButton btnAddToFav;
    private RecyclerView rvMainContent;

    private MealHeaderAdapter headerAdapter;
    private IngredientsListAdapter ingredientsAdapter;
    private MealFooterAdapter footerAdapter;

    private MealDetailsContract.Presenter presenter;
    private RemoteMeal meal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MealDetailsPresenter();

        if (getArguments() != null) {
            MealDetailsFragmentArgs args = MealDetailsFragmentArgs.fromBundle(getArguments());
            meal = args.getMeal();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupToolbar();
        setupAdapters();
        setupClickListeners();

        presenter.attachView(this);

        if (meal != null) {
            presenter.loadMealDetails(meal);
        }
    }

    private void initViews(View view) {
        imgMealDetail = view.findViewById(R.id.imgMealDetail);
        toolbar = view.findViewById(R.id.toolbar);
        btnAddToFav = view.findViewById(R.id.btnAddToFav);
        rvMainContent = view.findViewById(R.id.rvMainContent);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            if (isAdded() && getView() != null) {
                Navigation.findNavController(requireView()).navigateUp();
            }
        });
    }

    private void setupAdapters() {
        headerAdapter = new MealHeaderAdapter(this);

        ingredientsAdapter = new IngredientsListAdapter();

        footerAdapter = new MealFooterAdapter();
        footerAdapter.setLifecycleOwner(getViewLifecycleOwner());
        footerAdapter.setInstructionsVisible(false);

        ConcatAdapter concatAdapter = new ConcatAdapter(headerAdapter, ingredientsAdapter, footerAdapter);

        rvMainContent.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMainContent.setAdapter(concatAdapter);
    }

    private void setupClickListeners() {
        btnAddToFav.setOnClickListener(v -> presenter.onAddToFavoritesClicked());
    }

    @Override
    public void showMealDetails(RemoteMeal meal) {
        if (!isAdded() || getView() == null) return;

        Glide.with(this)
                .load(meal.getThumbnail())
                .centerCrop()
                .placeholder(R.drawable.welcome_background)
                .into(imgMealDetail);

        headerAdapter.setMeal(meal);

        footerAdapter.setData(meal.getInstructions(), meal.getYoutube());
    }

    @Override
    public void showIngredients(List<IngredientItem> ingredients) {
        if (!isAdded() || getView() == null) return;
        ingredientsAdapter.setIngredients(ingredients);
    }

    @Override
    public void showInstructions(String instructions, String youtubeUrl) {
        if (!isAdded() || getView() == null) return;
        footerAdapter.setData(instructions, youtubeUrl);
    }

    @Override
    public void showAddToFavoritesMessage() {
        if (!isAdded() || getContext() == null) return;
        Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showError(String message) {
        showSnackBarError(message);
    }

    @Override
    public void onIngredientsTabSelected() {
        footerAdapter.setInstructionsVisible(false);
    }

    @Override
    public void onInstructionsTabSelected() {
        footerAdapter.setInstructionsVisible(true);
    }

    @Override
    public void onAddToPlanClicked() {
        if (!isAdded() || getContext() == null) return;
        Toast.makeText(getContext(), "Add to Plan - Coming Soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
