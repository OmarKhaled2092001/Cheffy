package com.example.cheffy.ui.home.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.RemoteMeal;

import java.util.ArrayList;
import java.util.List;

public class PopularMealsAdapter extends RecyclerView.Adapter<PopularMealsAdapter.MealViewHolder> {

    private List<RemoteMeal> meals = new ArrayList<>();
    private final OnMealClickListener listener;



    public PopularMealsAdapter(OnMealClickListener listener) {
        this.listener = listener;
    }

    public void setMeals(List<RemoteMeal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_meal, parent, false);
        return new MealViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        RemoteMeal meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPopularMeal;
        private final TextView tvPopularMealName;
        private final TextView tvPopularMealArea;
        private RemoteMeal currentMeal;


        MealViewHolder(@NonNull View itemView, OnMealClickListener listener) {
            super(itemView);
            ivPopularMeal = itemView.findViewById(R.id.ivPopularMeal);
            tvPopularMealName = itemView.findViewById(R.id.tvPopularMealName);
            tvPopularMealArea = itemView.findViewById(R.id.tvPopularMealArea);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null && currentMeal != null) {
                    listener.onMealClick(currentMeal);
                }
            });
        }

        void bind(RemoteMeal meal) {
            this.currentMeal = meal;

            tvPopularMealName.setText(meal.getName());
            tvPopularMealArea.setText(meal.getArea());

            Glide.with(itemView.getContext())
                    .load(meal.getThumbnail())
                    .centerCrop()
                    .placeholder(R.drawable.welcome_background)
                    .into(ivPopularMeal);
        }
    }
}
