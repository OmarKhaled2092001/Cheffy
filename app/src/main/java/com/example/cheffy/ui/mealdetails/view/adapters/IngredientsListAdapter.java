package com.example.cheffy.ui.mealdetails.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.ui.mealdetails.model.IngredientItem;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientViewHolder> {

    private List<IngredientItem> ingredients = new ArrayList<>();
    private boolean isVisible = true;

    public void setIngredients(List<IngredientItem> ingredients) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setVisible(boolean visible) {
        if (this.isVisible != visible) {
            this.isVisible = visible;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_row_clean, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientItem item = ingredients.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return isVisible ? ingredients.size() : 0;
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgIngredient;
        private final TextView tvIngredientName;
        private final TextView tvIngredientMeasure;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIngredient = itemView.findViewById(R.id.imgIngredient);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            tvIngredientMeasure = itemView.findViewById(R.id.tvIngredientMeasure);
        }

        public void bind(IngredientItem item) {
            tvIngredientName.setText(item.getName());
            tvIngredientMeasure.setText(item.getMeasure());

            Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.welcome_background)
                    .into(imgIngredient);
        }
    }
}
