package com.example.cheffy.ui.search.view.adapter;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;

public interface OnMealClickListener {
    void onMealClick(RemoteMeal meal);
    void onFavoriteClick(RemoteMeal meal);
}
