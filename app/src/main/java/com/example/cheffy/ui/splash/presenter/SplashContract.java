package com.example.cheffy.ui.splash.presenter;

public interface SplashContract {

    interface View {
        void navigateToOnboarding();
        void navigateToWelcome();
        void navigateToHome();
    }

    interface Presenter {
        void decideNextScreen();
        void detachView();
    }
}
