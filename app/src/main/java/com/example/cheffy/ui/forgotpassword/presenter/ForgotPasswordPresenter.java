package com.example.cheffy.ui.forgotpassword.presenter;

import com.example.cheffy.data.auth.models.User;
import com.example.cheffy.data.auth.repository.AuthResultCallback;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.utils.ValidationUtils;

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {
    private ForgotPasswordContract.View view;
    private final IAuthRepository authRepository;

    public ForgotPasswordPresenter(ForgotPasswordContract.View view, IAuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(ForgotPasswordContract.View view) {
        this.view = view;
    }

    @Override
    public void sendResetLink(String email) {
        if (ValidationUtils.isEmpty(email)) {
            if (view != null) view.showEmailError("Email is required");
            return;
        }

        if (ValidationUtils.isInValidEmail(email)) {
            if (view != null) view.showEmailError("Invalid email address format");
            return;
        }

        if (view != null) view.showLoading();

        authRepository.sendPasswordResetEmail(email, new AuthResultCallback() {
            @Override
            public void onSuccess(User user) {
                if (view == null) return;
                view.hideLoading();
                view.showSuccessMessage("Reset link sent to your email. Please check your inbox.");
            }

            @Override
            public void onError(String message) {
                if (view == null) return;
                view.hideLoading();
                view.showErrorMessage(message != null ? message : "Failed to send reset email");
            }
        });
    }

    @Override
    public void onLoginClicked() {
        if (view != null) view.navigateToLogin();
    }

    @Override
    public void detachView() {
        view = null;
    }
}
