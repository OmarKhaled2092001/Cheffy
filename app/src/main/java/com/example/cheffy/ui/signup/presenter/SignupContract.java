package com.example.cheffy.ui.signup.presenter;

public interface SignupContract {
    interface View {
        void navigateToLoginScreen();
        void showFullNameError(String message);
        void showEmailError(String message);
        void showPasswordError(String message);
        void showConfirmPasswordError(String message);
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void attachView(SignupContract.View view);
        void registerUser(String fullName, String email, String password, String confirmPassword);
        void onLoginClicked();
        void detachView();

    }
}
