package com.example.cheffy.ui.mealslist.view.adapter;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;

public interface OnMealGridClickListener {
    void onMealClick(RemoteMeal meal);
    void onFavoriteClick(RemoteMeal meal);
}
