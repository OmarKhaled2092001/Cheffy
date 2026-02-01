package com.example.cheffy.ui.welcome.presenter;

public class WelcomePresenter implements WelcomeContract.Presenter {
    private WelcomeContract.View view;

    @Override
    public void attachView(WelcomeContract.View view) {
        this.view = view;
    }

    @Override
    public void onLoginClicked() {
        if (view != null) view.navigateToLoginScreen();
    }

    @Override
    public void onCreateAccountClicked() {
        if (view != null) view.navigateToSignupScreen();
    }

    @Override
    public void onGuestClicked() {
        if (view != null) view.navigateToHomeAsGuest();
    }

    @Override
    public void detachView() {
        view = null;
    }
}
