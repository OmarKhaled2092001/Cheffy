package com.example.cheffy.ui.home.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.Ingredient;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients = new ArrayList<>();
    private final OnIngredientClickListener listener;


    public IngredientsAdapter(OnIngredientClickListener listener) {
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
                .inflate(R.layout.item_circle_card, parent, false);
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
        private final ShapeableImageView imgCountry;
        private final TextView tvCountryName;
        private Ingredient currentIngredient;


        IngredientViewHolder(@NonNull View itemView, OnIngredientClickListener listener) {
            super(itemView);
            imgCountry = itemView.findViewById(R.id.imgItem);
            tvCountryName = itemView.findViewById(R.id.tvItem);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null && currentIngredient != null) {
                    listener.onIngredientClick(currentIngredient);
                }
            });
        }

        void bind(Ingredient ingredient) {
            this.currentIngredient = ingredient;

            tvCountryName.setText(ingredient.getName());
            Glide.with(itemView.getContext())
                    .load(ingredient.getThumbnail())
                    .centerCrop()
                    .placeholder(R.drawable.welcome_background)
                    .into(imgCountry);
        }
    }
}
