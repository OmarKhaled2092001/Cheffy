package com.example.cheffy.ui.mealdetails.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;

public class MealHeaderAdapter extends RecyclerView.Adapter<MealHeaderAdapter.HeaderViewHolder> {

    private RemoteMeal meal;
    private final MealHeaderListener mealHeaderListener;

    public void setMeal(RemoteMeal meal) {
        this.meal = meal;
        notifyDataSetChanged();
    }

    public MealHeaderAdapter(MealHeaderListener mealHeaderListener) {
        this.mealHeaderListener = mealHeaderListener;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_header, parent, false);
        return new HeaderViewHolder(view, mealHeaderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {
        if (meal != null) {
            holder.bind(meal);
        }
    }

    @Override
    public int getItemCount() {
        return meal != null ? 1 : 0;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvMealName;
        private final Chip chipCategory, chipArea;
        private final TabLayout tabLayout;
        private final MaterialButton btnAddToPlan;

        private final MealHeaderListener mealHeaderListener;

        public HeaderViewHolder(@NonNull View itemView, MealHeaderListener mealHeaderListener) {
            super(itemView);
            this.mealHeaderListener = mealHeaderListener;

            tvMealName = itemView.findViewById(R.id.tvMealName);
            chipCategory = itemView.findViewById(R.id.chipCategory);
            chipArea = itemView.findViewById(R.id.chipArea);
            tabLayout = itemView.findViewById(R.id.tabLayout);
            btnAddToPlan = itemView.findViewById(R.id.btnAddToPlan);

            setupTabLayout();
            setupAddToPlanButton();
        }

        private void setupTabLayout() {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (mealHeaderListener != null) {
                        if (tab.getPosition() == 0) {
                            mealHeaderListener.onIngredientsTabSelected();
                        } else {
                            mealHeaderListener.onInstructionsTabSelected();
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }

        private void setupAddToPlanButton() {
            btnAddToPlan.setOnClickListener(v -> {
                if (mealHeaderListener != null) {
                    mealHeaderListener.onAddToPlanClicked();
                }
            });
        }

        public void bind(RemoteMeal meal) {
            tvMealName.setText(meal.getName());

            if (meal.getCategory() != null && !meal.getCategory().isEmpty()) {
                chipCategory.setText(meal.getCategory());
                chipCategory.setVisibility(View.VISIBLE);
            } else {
                chipCategory.setVisibility(View.GONE);
            }

            if (meal.getArea() != null && !meal.getArea().isEmpty()) {
                chipArea.setText(meal.getArea());
                chipArea.setVisibility(View.VISIBLE);
            } else {
                chipArea.setVisibility(View.GONE);
            }
        }
    }
}
