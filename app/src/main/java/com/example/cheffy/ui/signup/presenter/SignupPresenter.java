package com.example.cheffy.ui.signup.presenter;

import com.example.cheffy.data.auth.models.User;
import com.example.cheffy.data.auth.repository.AuthResultCallback;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.utils.ValidationUtils;

public class SignupPresenter implements SignupContract.Presenter {

    private SignupContract.View view;
    private final IAuthRepository authRepository;

    public SignupPresenter(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(SignupContract.View view) {
        this.view = view;
    }

    @Override
    public void registerUser(String fullName, String email, String password, String confirmPassword) {
        if (ValidationUtils.isEmpty(fullName)) {
            if (view != null) view.showFullNameError("Full name is required");
            return;
        }

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

        if (!ValidationUtils.isValidPassword(password)) {
            if (view != null) {
                view.showPasswordError("Password is too weak. Must contain:\n" +
                        "- At least 8 characters\n" +
                        "- One uppercase letter (A-Z)\n" +
                        "- One number (0-9)\n" +
                        "- One special char (@#$%^&+=)");
            }
            return;
        }

        if (!password.equals(confirmPassword)) {
            if (view != null) view.showConfirmPasswordError("Passwords do not match");
            return;
        }

        if (view != null) view.showLoading();

        authRepository.register(email, password, fullName, new AuthResultCallback() {
            @Override
            public void onSuccess(User user) {
                if (view == null) return;
                view.hideLoading();
                view.showSuccessMessage("User registered successfully");
                view.navigateToLoginScreen();
            }

            @Override
            public void onError(String message) {
                if (view == null) return;
                view.hideLoading();
                view.showErrorMessage(message != null ? message : "Registration failed");
            }
        });
    }

    @Override
    public void onLoginClicked() {
        if (view != null) view.navigateToLoginScreen();
    }

    @Override
    public void detachView() {
        view = null;
    }

}
