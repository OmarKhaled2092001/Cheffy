package com.example.cheffy.ui.plan.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheffy.R;
import com.example.cheffy.common.base.BaseFragment;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.repository.MealsRepositoryImpl;
// import com.example.cheffy.databinding.FragmentPlanBinding; // REMOVED
import com.example.cheffy.ui.plan.model.DayItem;
import com.example.cheffy.ui.plan.model.PlannedMeal;
import com.example.cheffy.ui.plan.presenter.PlanContract;
import com.example.cheffy.ui.plan.presenter.PlanPresenter;
import com.example.cheffy.ui.plan.view.adapters.CalendarDayAdapter;
import com.example.cheffy.ui.plan.view.adapters.PlanMealAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PlanFragment extends BaseFragment implements PlanContract.View {
    private RecyclerView rvCalendar;
    private RecyclerView rvPlannedMeals;
    private View btnAddToPlan;
    private View layoutEmptyPlan;
    private TextView tvSelectedDay;

    private PlanContract.Presenter presenter;
    private CalendarDayAdapter dayAdapter;
    private PlanMealAdapter mealAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PlanPresenter(MealsRepositoryImpl.getInstance(requireContext()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout directly using R.layout.fragment_plan
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        setupRecyclerViews();
        setupClickListeners();
        presenter.attachView(this);
    }

    private void initViews(View view) {
        rvCalendar = view.findViewById(R.id.rvCalendar);
        rvPlannedMeals = view.findViewById(R.id.rvPlannedMeals);
        btnAddToPlan = view.findViewById(R.id.btnAddToPlan);
        layoutEmptyPlan = view.findViewById(R.id.layoutEmptyPlan);
        tvSelectedDay = view.findViewById(R.id.tvSelectedDay);
    }


    private void setupRecyclerViews() {
        dayAdapter = new CalendarDayAdapter(this::onDayClicked);
        if (rvCalendar != null) {
            rvCalendar.setAdapter(dayAdapter);
        }

        mealAdapter = new PlanMealAdapter(
                this::onMealClicked,
                this::onDeleteClicked
        );
        if (rvPlannedMeals != null) {
            rvPlannedMeals.setAdapter(mealAdapter);
        }
    }

    private void setupClickListeners() {
        if (btnAddToPlan != null) {
            btnAddToPlan.setOnClickListener(v -> {
                if (isAdded() && getView() != null) {
                    Navigation.findNavController(requireView()).navigate(R.id.homeFragment);
                }
            });
        }
    }

    private void onDayClicked(String dayOfWeek) {
        presenter.onDaySelected(dayOfWeek);
        dayAdapter.setSelectedDay(dayOfWeek);
    }

    private void onMealClicked(PlannedMeal meal) {
        presenter.onMealClicked(meal);
    }

    private void onDeleteClicked(PlannedMeal meal) {
        presenter.onRemoveMealClicked(meal);
    }

    @Override
    public void showDays(List<DayItem> days) {
        if (!isAdded()) return;
        dayAdapter.setDays(days);
    }

    @Override
    public void renderMeals(List<PlannedMeal> meals) {
        if (!isAdded()) return;

        if (rvPlannedMeals != null) rvPlannedMeals.setVisibility(View.VISIBLE);
        if (layoutEmptyPlan != null) layoutEmptyPlan.setVisibility(View.GONE);

        mealAdapter.submitList(meals);
    }

    @Override
    public void showEmptyState() {
        if (!isAdded()) return;

        if (rvPlannedMeals != null) rvPlannedMeals.setVisibility(View.GONE);
        if (layoutEmptyPlan != null) layoutEmptyPlan.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyState() {
        if (!isAdded()) return;

        if (rvPlannedMeals != null) rvPlannedMeals.setVisibility(View.VISIBLE);
        if (layoutEmptyPlan != null) layoutEmptyPlan.setVisibility(View.GONE);
    }

    @Override
    public void updateSelectedDay(String dayName) {
        if (!isAdded() || tvSelectedDay == null) return;
        tvSelectedDay.setText(getString(R.string.meals_for_day, dayName));
    }

    @Override
    public void showError(String message) {
        showSnackBarError(message);
    }

    @Override
    public void showUndoSnackbar(PlannedMeal plannedMeal) {
        if (!isAdded() || getView() == null) return;

        Snackbar snackbar = Snackbar.make(
                requireView(),
                R.string.meal_removed_from_plan,
                Snackbar.LENGTH_LONG
        );
        snackbar.setAction(R.string.undo, v -> presenter.onUndoRemove(plannedMeal));
        snackbar.setAnchorView(R.id.bottom_navigation);
        snackbar.show();
    }

    @Override
    public void navigateToMealDetails(RemoteMeal meal) {
        if (!isAdded() || getView() == null || meal == null) return;

        PlanFragmentDirections.ActionNavPlanToMealDetailsFragment action =
                PlanFragmentDirections.actionNavPlanToMealDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }

        rvCalendar = null;
        rvPlannedMeals = null;
        btnAddToPlan = null;
        layoutEmptyPlan = null;
        tvSelectedDay = null;
    }
}