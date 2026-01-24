package com.example.cheffy.ui.welcome.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cheffy.R;
import com.example.cheffy.ui.welcome.presenter.WelcomeContract;
import com.example.cheffy.ui.welcome.presenter.WelcomePresenter;

public class WelcomeFragment extends Fragment implements WelcomeContract.View {
    private WelcomeContract.Presenter presenter;
    private Button btnLogin, btnSignup, btnGuest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WelcomePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);
        initViews(view);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLoginClicked();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCreateAccountClicked();
            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onGuestClicked();
            }
        });


    }

    private void initViews(View view) {
        btnLogin = view.findViewById(R.id.btn_login);
        btnSignup = view.findViewById(R.id.btn_create_account);
        btnGuest = view.findViewById(R.id.btn_guest);
    }

    @Override
    public void navigateToLoginScreen() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_welcomeFragment_to_loginFragment);
        }
    }

    @Override
    public void navigateToSignupScreen() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_welcomeFragment_to_signupFragment);
        }
    }

    @Override
    public void navigateToHomeAsGuest() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_welcomeFragment_to_homeFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}