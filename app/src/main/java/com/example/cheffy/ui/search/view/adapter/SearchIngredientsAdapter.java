package com.example.cheffy.ui.search.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.remote.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class SearchIngredientsAdapter extends RecyclerView.Adapter<SearchIngredientsAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients = new ArrayList<>();
    private final OnIngredientClickListener listener;

    public SearchIngredientsAdapter(OnIngredientClickListener listener) {
        this.listener = listener;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_circle_grid, parent, false);
        return new IngredientViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgItem;
        private final TextView tvItem;
        private Ingredient currentIngredient;

        IngredientViewHolder(@NonNull View itemView, OnIngredientClickListener listener) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvItem = itemView.findViewById(R.id.tvItem);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null && currentIngredient != null) {
                    listener.onIngredientClick(currentIngredient);
                }
            });
        }

        void bind(Ingredient ingredient) {
            this.currentIngredient = ingredient;
            tvItem.setText(ingredient.getName());

            Glide.with(itemView.getContext())
                    .load(ingredient.getThumbnail())
                    .placeholder(R.drawable.welcome_background)
                    .error(R.drawable.welcome_background)
                    .centerInside()
                    .into(imgItem);
        }
    }
}
