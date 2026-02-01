package com.example.cheffy.ui.welcome.presenter;

public interface WelcomeContract {
    interface View {
        void navigateToLoginScreen();
        void navigateToSignupScreen();
        void navigateToHomeAsGuest();
    }

    interface Presenter {
        void attachView(View view);
        void onLoginClicked();
        void onCreateAccountClicked();
        void onGuestClicked();
        void detachView();

    }
}
