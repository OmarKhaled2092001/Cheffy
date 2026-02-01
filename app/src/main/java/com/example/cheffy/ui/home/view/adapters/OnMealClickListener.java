package com.example.cheffy.ui.home.view.adapters;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;

public interface OnMealClickListener {
    void onMealClick(RemoteMeal meal);
    void onFavoriteClick(RemoteMeal meal);
}
