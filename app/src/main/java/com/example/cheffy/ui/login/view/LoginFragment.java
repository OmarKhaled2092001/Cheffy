package com.example.cheffy.ui.login.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.cheffy.R;
import com.example.cheffy.data.auth.repository.AuthRepositoryImpl;
import com.example.cheffy.data.auth.social.GoogleAuthHelper;
import com.example.cheffy.data.auth.social.SocialAuthCallback;
import com.example.cheffy.common.base.BaseFragment;
import com.example.cheffy.ui.login.presenter.LoginContract;
import com.example.cheffy.ui.login.presenter.LoginPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends BaseFragment implements LoginContract.View {

    private TextInputEditText etEmail, etPassword;
    private TextInputLayout tilEmail, tilPassword;
    private Button btnLogin;
    private ImageButton btnFacebook, btnGoogle, btnTwitter;
    private TextView tvForgotPass, tvCreateAccount;
    private ProgressBar progressBar;

    private LoginContract.Presenter presenter;
    private GoogleAuthHelper googleAuthHelper;
    private final SocialAuthCallback socialAuthCallback = new SocialAuthCallback() {
        @Override
        public void onSuccess(String idToken) {
            requireActivity().runOnUiThread(() -> {
                presenter.firebaseAuthWithGoogle(idToken);
            });
        }

        @Override
        public void onError(String message) {
            requireActivity().runOnUiThread(() -> {
                hideLoading();
                showErrorMessage(message);
            });
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginPresenter(AuthRepositoryImpl.getInstance());
        googleAuthHelper = new GoogleAuthHelper(requireContext(), socialAuthCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        initViews(view);

        setupClearErrorOnTyping(tilEmail, etEmail);
        setupClearErrorOnTyping(tilPassword, etPassword);

        setupListeners();
    }

    private void initViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);

        tilEmail = view.findViewById(R.id.tilEmail);
        tilPassword = view.findViewById(R.id.tilPassword);

        btnLogin = view.findViewById(R.id.btnLogin);

        btnFacebook = view.findViewById(R.id.btnFacebook);
        btnGoogle = view.findViewById(R.id.btnGoogle);
        btnTwitter = view.findViewById(R.id.btnTwitter);

        tvForgotPass = view.findViewById(R.id.tvForgotPass);
        tvCreateAccount = view.findViewById(R.id.tvCreateAccount);

        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            String email = getText(etEmail);
            String password = getText(etPassword);
            presenter.loginUser(email, password);
        });

        tvForgotPass.setOnClickListener(v ->
                presenter.onForgotPasswordClicked()
        );

        tvCreateAccount.setOnClickListener(v ->
                presenter.onCreateAccountClicked()
        );

        btnFacebook.setOnClickListener(v -> {

        });

        btnGoogle.setOnClickListener(v -> {
            presenter.onGoogleLoginClicked();
        });

        btnTwitter.setOnClickListener(v -> {

        });

    }


    @Override
    public void showLoading() {
        hideKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
    }

    @Override
    public void showEmailError(String message) {
        tilEmail.setErrorEnabled(true);
        tilEmail.setError(message);
        etEmail.requestFocus();
    }

    @Override
    public void showPasswordError(String message) {
        tilPassword.setErrorEnabled(true);
        tilPassword.setError(message);
        etPassword.requestFocus();
    }

    @Override
    public void navigateToHome() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);
        }
    }

    @Override
    public void navigateToSignup() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_signupFragment);
        }
    }

    @Override
    public void navigateToForgotPassword() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
        }
    }

    @Override
    public void launchFacebookLogin() {

    }

    @Override
    public void launchGoogleLogin() {
        showLoading();
        googleAuthHelper.launchSignIn();
    }

    @Override
    public void launchTwitterLogin() {

    }

    @Override
    public void showErrorMessage(String message) {
        showSnackBarError(message);
    }

    @Override
    public void showSuccessMessage(String message) {
        showSnackBarSuccess(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}