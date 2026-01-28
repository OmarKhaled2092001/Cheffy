package com.example.cheffy.ui.splash.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.data.local.AppPreferences;

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View view;
    private final AppPreferences appPreferences;
    private final IAuthRepository authRepository;

    public SplashPresenter(AppPreferences appPreferences, IAuthRepository authRepository) {
        this.appPreferences = appPreferences;
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(SplashContract.View view) {
        this.view = view;
    }

    @Override
    public void decideNextScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (view == null) return;

            if (appPreferences.isFirstRun()) {
                view.navigateToOnboarding();
                return;
            }

            if (authRepository.isUserLoggedIn()) {
                view.navigateToHome();
            } else {
                view.navigateToWelcome();
            }

        }, 3000);
    }

    @Override
    public void detachView() {
        view = null;
    }
}
