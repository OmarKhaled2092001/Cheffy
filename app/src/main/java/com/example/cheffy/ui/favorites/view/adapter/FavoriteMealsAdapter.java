package com.example.cheffy.ui.favorites.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FavoriteMealsAdapter 
        extends ListAdapter<RemoteMeal, FavoriteMealsAdapter.MealViewHolder> {

    private static final DiffUtil.ItemCallback<RemoteMeal> DIFF_CALLBACK = 
            new DiffUtil.ItemCallback<RemoteMeal>() {
                @Override
                public boolean areItemsTheSame(@NonNull RemoteMeal oldItem, 
                                               @NonNull RemoteMeal newItem) {
                    return oldItem.getIdMeal().equals(newItem.getIdMeal());
                }

                @Override
                public boolean areContentsTheSame(@NonNull RemoteMeal oldItem, 
                                                  @NonNull RemoteMeal newItem) {
                    return oldItem.getName().equals(newItem.getName())
                            && safeEquals(oldItem.getThumbnail(), newItem.getThumbnail())
                            && safeEquals(oldItem.getArea(), newItem.getArea());
                }
                
                private boolean safeEquals(String a, String b) {
                    if (a == null && b == null) return true;
                    if (a == null || b == null) return false;
                    return a.equals(b);
                }
            };

    private final OnFavoriteInteractionListener listener;

    public FavoriteMealsAdapter(OnFavoriteInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fav_meal, parent, false);
        return new MealViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        RemoteMeal meal = getItem(position);
        holder.bind(meal);
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMealImage;
        private final TextView tvMealName;
        private final TextView tvMealArea;

        private RemoteMeal currentMeal;

        MealViewHolder(@NonNull View itemView, OnFavoriteInteractionListener listener) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.ivPopularMeal);
            tvMealName = itemView.findViewById(R.id.tvPopularMealName);
            tvMealArea = itemView.findViewById(R.id.tvPopularMealArea);
            FloatingActionButton btnDelete = itemView.findViewById(R.id.btnAddToFav);

            itemView.setOnClickListener(v -> {
                if (listener != null && currentMeal != null) {
                    listener.onMealClick(currentMeal);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null && currentMeal != null) {
                    listener.onDeleteClick(currentMeal);
                }
            });
        }

        void bind(RemoteMeal meal) {
            this.currentMeal = meal;

            tvMealName.setText(meal.getName());
            tvMealArea.setText(meal.getArea() != null ? meal.getArea() : "");

            Glide.with(itemView.getContext())
                    .load(meal.getThumbnail())
                    .centerCrop()
                    .placeholder(R.drawable.welcome_background)
                    .into(ivMealImage);
        }
    }
}
