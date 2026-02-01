package com.example.cheffy.ui.signup.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.utils.ValidationUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class SignupPresenter extends BasePresenter<SignupContract.View> implements SignupContract.Presenter {

    private final IAuthRepository authRepository;

    public SignupPresenter(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(SignupContract.View view) {
        super.attachView(view);
    }

    @Override
    public void registerUser(String fullName, String email, String password, String confirmPassword) {
        if (ValidationUtils.isEmpty(fullName)) {
            if (isViewAttached()) view.showFullNameError("Full name is required");
            return;
        }

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

        if (!ValidationUtils.isValidPassword(password)) {
            if (isViewAttached()) {
                view.showPasswordError("Password is too weak. Must contain:\n" +
                        "- At least 8 characters\n" +
                        "- One uppercase letter (A-Z)\n" +
                        "- One number (0-9)\n" +
                        "- One special char (@#$%^&+=)");
            }
            return;
        }

        if (!password.equals(confirmPassword)) {
            if (isViewAttached()) view.showConfirmPasswordError("Passwords do not match");
            return;
        }

        if (isViewAttached()) view.showLoading();

        addDisposable(
            authRepository.register(email, password, fullName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    user -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showSuccessMessage("User registered successfully");
                            view.navigateToLoginScreen();
                        }
                    },
                    throwable -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showErrorMessage(throwable.getMessage() != null 
                                ? throwable.getMessage() 
                                : "Registration failed");
                        }
                    }
                )
        );
    }

    @Override
    public void onLoginClicked() {
        if (isViewAttached()) view.navigateToLoginScreen();
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
