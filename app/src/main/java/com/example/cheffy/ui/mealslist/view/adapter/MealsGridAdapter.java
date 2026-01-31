package com.example.cheffy.ui.mealslist.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MealsGridAdapter extends RecyclerView.Adapter<MealsGridAdapter.MealViewHolder> {

    private List<RemoteMeal> meals = new ArrayList<>();
    private Set<String> favoriteIds = new HashSet<>();
    private final OnMealGridClickListener listener;

    public MealsGridAdapter(OnMealGridClickListener listener) {
        this.listener = listener;
    }

    public void setMeals(List<RemoteMeal> meals) {
        this.meals = meals != null ? meals : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setFavoriteIds(Set<String> favoriteIds) {
        this.favoriteIds = favoriteIds != null ? favoriteIds : new HashSet<>();
        notifyDataSetChanged();
    }

    public void updateFavoriteStatus(String mealId, boolean isFavorite) {
        if (isFavorite) {
            favoriteIds.add(mealId);
        } else {
            favoriteIds.remove(mealId);
        }
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getIdMeal().equals(mealId)) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    public boolean isFavorite(String mealId) {
        return favoriteIds.contains(mealId);
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_grid, parent, false);
        return new MealViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        RemoteMeal meal = meals.get(position);
        boolean isFav = favoriteIds.contains(meal.getIdMeal());
        holder.bind(meal, isFav);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivMealImage;
        private final TextView tvMealName;
        private final TextView tvMealArea;
        private final ImageView btnAddToFav;

        private RemoteMeal currentMeal;

        MealViewHolder(@NonNull View itemView, OnMealGridClickListener listener) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.ivPopularMeal);
            tvMealName = itemView.findViewById(R.id.tvPopularMealName);
            tvMealArea = itemView.findViewById(R.id.tvPopularMealArea);
            btnAddToFav = itemView.findViewById(R.id.btnAddToFav);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onMealClick(currentMeal);
                }
            });

            btnAddToFav.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null && currentMeal != null) {
                    listener.onFavoriteClick(currentMeal);
                }
            });
        }

        void bind(RemoteMeal meal, boolean isFavorite) {
            this.currentMeal = meal;

            tvMealName.setText(meal.getName());

            if (meal.getArea() != null && !meal.getArea().isEmpty()) {
                tvMealArea.setText(meal.getArea());
                tvMealArea.setVisibility(View.VISIBLE);
            } else {
                tvMealArea.setVisibility(View.GONE);
            }

            if (isFavorite) {
                btnAddToFav.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                btnAddToFav.setImageResource(R.drawable.favorite);
            }

            Glide.with(itemView.getContext())
                    .load(meal.getThumbnail())
                    .placeholder(R.drawable.welcome_background)
                    .centerCrop()
                    .into(ivMealImage);
        }
    }
}

