package com.example.cheffy.ui.signup.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.cheffy.R;
import com.example.cheffy.data.auth.repository.AuthRepositoryImpl;
import com.example.cheffy.common.base.BaseFragment;
import com.example.cheffy.ui.signup.presenter.SignupContract;
import com.example.cheffy.ui.signup.presenter.SignupPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignupFragment extends BaseFragment implements SignupContract.View {
    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword;
    private TextInputLayout tilFullName, tilEmail, tilPassword, tilConfirmPassword;
    private Button btnSignup;
    private TextView tvLoginHere;
    private ProgressBar progressBar;
    private SignupContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SignupPresenter(AuthRepositoryImpl.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        initViews(view);

        setupClearErrorOnTyping(tilFullName, etFullName);
        setupClearErrorOnTyping(tilEmail, etEmail);
        setupClearErrorOnTyping(tilPassword, etPassword);
        setupClearErrorOnTyping(tilConfirmPassword, etConfirmPassword);

        setupListeners();
    }

    private void initViews(View view) {
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);

        tilFullName = view.findViewById(R.id.tilFullName);
        tilEmail = view.findViewById(R.id.tilEmail);
        tilPassword = view.findViewById(R.id.tilPassword);
        tilConfirmPassword = view.findViewById(R.id.tilConfirmPassword);

        btnSignup = view.findViewById(R.id.btnSignup);
        tvLoginHere = view.findViewById(R.id.tvLoginHere);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnSignup.setOnClickListener(v -> {
            String fullName = getText(etFullName);
            String email = getText(etEmail);
            String password = getText(etPassword);
            String confirmPassword = getText(etConfirmPassword);

            presenter.registerUser(fullName, email, password, confirmPassword);
        });

        tvLoginHere.setOnClickListener(v -> {
            presenter.onLoginClicked();
        });
    }


    @Override
    public void navigateToLoginScreen() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_signupFragment_to_loginFragment);
        }
    }

    @Override
    public void showFullNameError(String message) {
        tilFullName.setErrorEnabled(true);
        tilFullName.setError(message);
        etFullName.requestFocus();
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
    public void showConfirmPasswordError(String message) {
        tilConfirmPassword.setErrorEnabled(true);
        tilConfirmPassword.setError(message);
        etConfirmPassword.requestFocus();
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
    public void showLoading() {
        hideKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        btnSignup.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnSignup.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}