package com.example.cheffy.ui.plan.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.ui.plan.model.PlannedMeal;

public class PlanMealAdapter extends ListAdapter<PlannedMeal, PlanMealAdapter.MealViewHolder> {

    private final OnMealClickListener mealClickListener;
    private final OnDeleteClickListener deleteClickListener;

    public interface OnMealClickListener {
        void onMealClicked(PlannedMeal meal);
    }

    public interface OnDeleteClickListener {
        void onDeleteClicked(PlannedMeal meal);
    }

    private static final DiffUtil.ItemCallback<PlannedMeal> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlannedMeal>() {
        @Override
        public boolean areItemsTheSame(@NonNull PlannedMeal oldItem, @NonNull PlannedMeal newItem) {
            return oldItem.getPlanId() == newItem.getPlanId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlannedMeal oldItem, @NonNull PlannedMeal newItem) {
            return oldItem.getPlanId() == newItem.getPlanId() && (oldItem.getName() != null && oldItem.getName().equals(newItem.getName()));
        }
    };

    public PlanMealAdapter(OnMealClickListener mealClickListener, OnDeleteClickListener deleteClickListener) {
        super(DIFF_CALLBACK);
        this.mealClickListener = mealClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_meal, parent, false);
        return new MealViewHolder(view, mealClickListener, deleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        PlannedMeal meal = getItem(position);
        holder.bind(meal);
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgMeal;
        private final TextView tvMealName;
        private final TextView tvMealArea;

        private PlannedMeal currentMeal;

        MealViewHolder(@NonNull View itemView, OnMealClickListener mealClickListener, OnDeleteClickListener deleteClickListener) {
            super(itemView);
            imgMeal = itemView.findViewById(R.id.imgMeal);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealArea = itemView.findViewById(R.id.tvMealArea);
            ImageButton btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(v -> {
                if (mealClickListener != null) {
                    mealClickListener.onMealClicked(currentMeal);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClicked(currentMeal);
                }
            });
        }


        void bind(PlannedMeal plannedMeal) {
            this.currentMeal = plannedMeal;
            tvMealName.setText(plannedMeal.getName());

            String areaText = "";
            if (plannedMeal.getCategory() != null && !plannedMeal.getCategory().isEmpty()) {
                areaText = plannedMeal.getCategory();
            }
            if (plannedMeal.getArea() != null && !plannedMeal.getArea().isEmpty()) {
                if (!areaText.isEmpty()) {
                    areaText += " • ";
                }
                areaText += plannedMeal.getArea();
            }
            tvMealArea.setText(areaText);

            Glide.with(itemView.getContext()).load(plannedMeal.getThumbnail()).centerCrop().placeholder(R.drawable.welcome_background).into(imgMeal);

        }
    }
}
