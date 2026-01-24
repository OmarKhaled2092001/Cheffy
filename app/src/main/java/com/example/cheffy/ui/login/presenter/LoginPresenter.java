package com.example.cheffy.ui.login.presenter;

import com.example.cheffy.data.auth.models.User;
import com.example.cheffy.data.auth.repository.AuthResultCallback;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.utils.ValidationUtils;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private final IAuthRepository authRepository;

    public LoginPresenter(LoginContract.View view, IAuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void loginUser(String email, String password) {
        if (ValidationUtils.isEmpty(email)) {
            if (view != null) view.showEmailError("Email is required");
            return;
        }

        if (ValidationUtils.isInValidEmail(email)) {
            if (view != null) view.showEmailError("Invalid email address format");
            return;
        }

        if (ValidationUtils.isEmpty(password)) {
            if (view != null) view.showPasswordError("Password is required");
            return;
        }

        if (view != null) view.showLoading();

        authRepository.login(email, password, new AuthResultCallback() {
            @Override
            public void onSuccess(User user) {
                if (view == null) return;
                view.hideLoading();
                view.showSuccessMessage("Login Successful");
                view.navigateToHome();
            }

            @Override
            public void onError(String message) {
                if (view == null) return;
                view.hideLoading();
                view.showErrorMessage(message != null ? message : "Login failed");
            }
        });
    }

    @Override
    public void onCreateAccountClicked() {
        if (view != null) view.navigateToSignup();
    }

    @Override
    public void onForgotPasswordClicked() {
        if (view != null) view.navigateToForgotPassword();
    }

    @Override
    public void onFacebookLoginClicked() {
        if (view != null) view.showErrorMessage("Facebook Login coming soon!");
    }

    @Override
    public void onGoogleLoginClicked() {
        if (view != null) view.launchGoogleLogin();
    }

    @Override
    public void firebaseAuthWithGoogle(String idToken) {
        if (view != null) view.showLoading();

        authRepository.loginWithGoogle(idToken, new AuthResultCallback() {
            @Override
            public void onSuccess(User user) {
                if (view == null) return;
                view.hideLoading();
                view.showSuccessMessage("Google Sign-In Successful");
                view.navigateToHome();
            }

            @Override
            public void onError(String message) {
                if (view == null) return;
                view.hideLoading();
                view.showErrorMessage(message != null ? message : "Google Auth Failed");
            }
        });
    }

    @Override
    public void onTwitterLoginClicked() {
        if (view != null) view.showErrorMessage("Twitter Login coming soon!");
    }

    @Override
    public void detachView() {
        view = null;
    }
}
