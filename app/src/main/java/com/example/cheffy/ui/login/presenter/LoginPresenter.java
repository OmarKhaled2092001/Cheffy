package com.example.cheffy.ui.login.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.utils.ValidationUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private final IAuthRepository authRepository;

    public LoginPresenter(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(LoginContract.View view) {
        super.attachView(view);
    }

    @Override
    public void loginUser(String email, String password) {
        if (ValidationUtils.isEmpty(email)) {
            if (isViewAttached()) view.showEmailError("Email is required");
            return;
        }

        if (ValidationUtils.isInValidEmail(email)) {
            if (isViewAttached()) view.showEmailError("Invalid email address format");
            return;
        }

        if (ValidationUtils.isEmpty(password)) {
            if (isViewAttached()) view.showPasswordError("Password is required");
            return;
        }

        if (isViewAttached()) view.showLoading();

        addDisposable(
            authRepository.login(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    user -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showSuccessMessage("Login Successful");
                            view.navigateToHome();
                        }
                    },
                    throwable -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showErrorMessage(throwable.getMessage() != null 
                                ? throwable.getMessage() 
                                : "Login failed");
                        }
                    }
                )
        );
    }

    @Override
    public void onCreateAccountClicked() {
        if (isViewAttached()) view.navigateToSignup();
    }

    @Override
    public void onForgotPasswordClicked() {
        if (isViewAttached()) view.navigateToForgotPassword();
    }

    @Override
    public void onFacebookLoginClicked() {
        if (isViewAttached()) view.showErrorMessage("Facebook Login coming soon!");
    }

    @Override
    public void onGoogleLoginClicked() {
        if (isViewAttached()) view.launchGoogleLogin();
    }

    @Override
    public void firebaseAuthWithGoogle(String idToken) {
        if (isViewAttached()) view.showLoading();

        addDisposable(
            authRepository.loginWithGoogle(idToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    user -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showSuccessMessage("Google Sign-In Successful");
                            view.navigateToHome();
                        }
                    },
                    throwable -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showErrorMessage(throwable.getMessage() != null 
                                ? throwable.getMessage() 
                                : "Google Auth Failed");
                        }
                    }
                )
        );
    }

    @Override
    public void onTwitterLoginClicked() {
        if (isViewAttached()) view.showErrorMessage("Twitter Login coming soon!");
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
