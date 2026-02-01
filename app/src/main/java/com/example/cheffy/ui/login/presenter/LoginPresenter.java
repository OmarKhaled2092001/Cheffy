package com.example.cheffy.ui.login.presenter;

import android.util.Log;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.utils.ValidationUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private static final String TAG = "LoginPresenter";
    private final IAuthRepository authRepository;
    private final IMealsRepository mealsRepository;

    public LoginPresenter(IAuthRepository authRepository, IMealsRepository mealsRepository) {
        this.authRepository = authRepository;
        this.mealsRepository = mealsRepository;
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

                            triggerPostLoginSync();
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

    private void triggerPostLoginSync() {
        addDisposable(
            mealsRepository.hasCloudData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    hasData -> {
                        if (hasData) {
                               if (isViewAttached()) {
                                view.showSuccessMessage("Restoring backup...");
                            }
                            addDisposable(
                                mealsRepository.restoreDataFromCloud()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        () -> {
                                            Log.d(TAG, "Cloud data restored successfully");
                                            if (isViewAttached()) view.navigateToHome();
                                        },
                                        error -> {
                                            Log.e(TAG, "Failed to restore cloud data", error);
                                            if (isViewAttached()) view.navigateToHome();
                                        }
                                    )
                            );
                        } else {
                            addDisposable(
                                mealsRepository.backupLocalDataToCloud()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        () -> Log.d(TAG, "Local data backed up to cloud"),
                                        error -> Log.e(TAG, "Failed to backup local data", error)
                                    )
                            );
                            if (isViewAttached()) view.navigateToHome();
                        }
                    },
                    error -> {
                        Log.e(TAG, "Failed to check cloud data", error);
                        if (isViewAttached()) view.navigateToHome();
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
                            triggerPostLoginSync();
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
