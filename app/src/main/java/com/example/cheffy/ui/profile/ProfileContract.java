package com.example.cheffy.ui.profile;

public interface ProfileContract {

    interface View {
        void showUserInfo(String displayName, String email, String photoUrl);

        void showFavoriteCount(int count);

        void showPlanCount(int count);

        void showToast(String message);

        void navigateToLogin();

        void showGuestMode();

        void showLoggedInMode();
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void loadUserProfile();

        void loadStats();

        void onLogoutClicked();

        void onThemeClicked();

        void onLanguageClicked();
    }
}
