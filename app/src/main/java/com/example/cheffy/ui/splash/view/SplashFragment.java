package com.example.cheffy.ui.splash.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cheffy.R;
import com.example.cheffy.data.auth.repository.AuthRepositoryImpl;
import com.example.cheffy.data.local.AppPreferences;
import com.example.cheffy.ui.splash.presenter.SplashContract;
import com.example.cheffy.ui.splash.presenter.SplashPresenter;

public class SplashFragment extends Fragment implements SplashContract.View {

    private SplashContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppPreferences appPreferences = new AppPreferences(requireContext());
        presenter = new SplashPresenter(this, appPreferences, new AuthRepositoryImpl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.decideNextScreen();
    }

    @Override
    public void navigateToOnboarding() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_splashFragment_to_onboardingFragment);
        }
    }

    @Override
    public void navigateToWelcome() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_splashFragment_to_welcomeFragment);
        }
    }

    @Override
    public void navigateToHome() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_splashFragment_to_homeFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
