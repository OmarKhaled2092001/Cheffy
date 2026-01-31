package com.example.cheffy.ui.favorites.view.adapter;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;

public interface OnFavoriteInteractionListener {

    void onMealClick(RemoteMeal meal);

    void onDeleteClick(RemoteMeal meal);
}
