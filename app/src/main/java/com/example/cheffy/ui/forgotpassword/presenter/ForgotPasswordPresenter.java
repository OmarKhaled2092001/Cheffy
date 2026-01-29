package com.example.cheffy.ui.forgotpassword.presenter;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.utils.ValidationUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordContract.View> implements ForgotPasswordContract.Presenter {
    
    private final IAuthRepository authRepository;

    public ForgotPasswordPresenter(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(ForgotPasswordContract.View view) {
        super.attachView(view);
    }

    @Override
    public void sendResetLink(String email) {
        if (ValidationUtils.isEmpty(email)) {
            if (isViewAttached()) view.showEmailError("Email is required");
            return;
        }

        if (ValidationUtils.isInValidEmail(email)) {
            if (isViewAttached()) view.showEmailError("Invalid email address format");
            return;
        }

        if (isViewAttached()) view.showLoading();

        addDisposable(
            authRepository.sendPasswordResetEmail(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showSuccessMessage("Reset link sent to your email. Please check your inbox.");
                        }
                    },
                    throwable -> {
                        if (isViewAttached()) {
                            view.hideLoading();
                            view.showErrorMessage(throwable.getMessage() != null 
                                ? throwable.getMessage() 
                                : "Failed to send reset email");
                        }
                    }
                )
        );
    }

    @Override
    public void onLoginClicked() {
        if (isViewAttached()) view.navigateToLogin();
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
