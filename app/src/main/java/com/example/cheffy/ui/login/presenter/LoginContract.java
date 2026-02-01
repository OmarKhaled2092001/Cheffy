package com.example.cheffy.ui.login.presenter;

public interface LoginContract {
    interface View {
        void showLoading();
        void hideLoading();

        void showEmailError(String message);
        void showPasswordError(String message);

        void navigateToHome();
        void navigateToSignup();
        void navigateToForgotPassword();
        void launchFacebookLogin();
        void launchGoogleLogin();
        void launchTwitterLogin();
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void attachView(LoginContract.View view);
        void loginUser(String email, String password);

        void onCreateAccountClicked();
        void onForgotPasswordClicked();
        void onFacebookLoginClicked();
        void onGoogleLoginClicked();
        void firebaseAuthWithGoogle(String idToken);
        void onTwitterLoginClicked();
        void detachView();
    }
}
