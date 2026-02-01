package com.example.cheffy.ui.favorites.presenter;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.ui.favorites.FavoriteFilterType;

import java.util.List;

public interface FavoriteContract {

    interface View {
        void renderFavorites(List<RemoteMeal> meals);

        void showEmptyState();

        void hideEmptyState();

        void showError(String message);

        void showUndoSnackbar(RemoteMeal meal);

        void navigateToMealDetails(RemoteMeal meal);
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void onMealClicked(RemoteMeal meal);

        void onRemoveFavorite(RemoteMeal meal);

        void onUndoRemove(RemoteMeal meal);

        void onFilterSelected(FavoriteFilterType filterType);

        void onSearchQueryChanged(String query);
    }
}
