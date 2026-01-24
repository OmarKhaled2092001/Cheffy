package com.example.cheffy.ui.forgotpassword.view;

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
import com.example.cheffy.ui.forgotpassword.presenter.ForgotPasswordContract;
import com.example.cheffy.ui.forgotpassword.presenter.ForgotPasswordPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordFragment extends BaseFragment implements ForgotPasswordContract.View {

    private TextInputEditText etEmailForgot;
    private TextInputLayout tilEmailForgot;
    private Button btnSubmit;
    private TextView tvLoginHere;
    private ProgressBar progressBar;

    private ForgotPasswordContract.Presenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ForgotPasswordPresenter(this, new AuthRepositoryImpl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);
        initViews(view);

        setupClearErrorOnTyping(tilEmailForgot, etEmailForgot);

        setupListeners();
    }

    private void initViews(View view) {
        etEmailForgot = view.findViewById(R.id.etEmailForgot);
        tilEmailForgot = view.findViewById(R.id.tilEmailForgot);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        tvLoginHere = view.findViewById(R.id.tvLoginHere);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(v -> {
            String email = getText(etEmailForgot);
            presenter.sendResetLink(email);
        });

        tvLoginHere.setOnClickListener(v -> presenter.onLoginClicked());
    }

    @Override
    public void showLoading() {
        hideKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnSubmit.setEnabled(true);
    }

    @Override
    public void showEmailError(String message) {
        tilEmailForgot.setErrorEnabled(true);
        tilEmailForgot.setError(message);
        etEmailForgot.requestFocus();
    }

    @Override
    public void showSuccessMessage(String message) {
        showSnackBarSuccess(message);
    }

    @Override
    public void showErrorMessage(String message) {
        showSnackBarError(message);
    }

    @Override
    public void navigateToLogin() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
        }
    }
}