package com.example.cheffy.ui.forgotpassword.presenter;

public interface ForgotPasswordContract {
    interface View {
        void showLoading();
        void hideLoading();

        void showEmailError(String message);

        void showSuccessMessage(String message);
        void showErrorMessage(String message);
        void navigateToLogin();
    }

    interface Presenter {
        void attachView(ForgotPasswordContract.View view);
        void sendResetLink(String email);

        void onLoginClicked();

        void detachView();
    }
}
