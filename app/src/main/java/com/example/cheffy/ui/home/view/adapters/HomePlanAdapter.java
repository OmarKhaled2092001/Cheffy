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
import com.example.cheffy.ui.plan.model.PlannedMeal;

import java.util.ArrayList;
import java.util.List;

public class HomePlanAdapter extends RecyclerView.Adapter<HomePlanAdapter.PlanViewHolder> {

    private List<PlannedMeal> meals = new ArrayList<>();
    private final OnMealClickListener listener;

    public HomePlanAdapter(OnMealClickListener listener) {
        this.listener = listener;
    }

    public void setMeals(List<PlannedMeal> meals) {
        this.meals = meals != null ? meals : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_plan, parent, false);
        return new PlanViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        holder.bind(meals.get(position));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPlanMeal;
        private final TextView tvPlanMealName;
        private PlannedMeal currentMeal;

        PlanViewHolder(@NonNull View itemView, OnMealClickListener listener) {
            super(itemView);
            imgPlanMeal = itemView.findViewById(R.id.imgPlanMeal);
            tvPlanMealName = itemView.findViewById(R.id.tvPlanMealName);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null && currentMeal != null) {
                    listener.onMealClick(currentMeal.getMeal());
                }
            });
        }

        void bind(PlannedMeal meal) {
            this.currentMeal = meal;
            tvPlanMealName.setText(meal.getName());

            Glide.with(itemView.getContext())
                    .load(meal.getThumbnail())
                    .centerCrop()
                    .placeholder(R.drawable.welcome_background)
                    .into(imgPlanMeal);
        }
    }
}
