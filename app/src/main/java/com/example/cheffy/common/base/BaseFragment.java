package com.example.cheffy.common.base;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cheffy.R;
import com.example.cheffy.utils.SnackbarHelper;
import com.google.android.material.textfield.TextInputLayout;

public abstract class BaseFragment extends Fragment {

    private View errorView;

    protected void hideKeyboard() {
        View view = this.getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void setupClearErrorOnTyping(TextInputLayout til, EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (til.isErrorEnabled()) {
                    til.setError(null);
                    til.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    protected void showSnackBarError(String message) {
        if (isAdded() && getView() != null) {
            SnackbarHelper.showError(getView(), message);
        }
    }

    protected void showSnackBarSuccess(String message) {
        if (isAdded() && getView() != null) {
            SnackbarHelper.showSuccess(getView(), message);
        }
    }

    protected String getText(EditText editText) {
        if (editText != null && editText.getText() != null) {
            return editText.getText().toString().trim();
        }
        return "";
    }


    public interface OnTryAgainListener {
        void onTryAgain();
    }

    protected void showErrorState(@IdRes int stubId, String message,
                                   @Nullable View[] contentViews,
                                   @Nullable OnTryAgainListener tryAgainListener) {
        if (!isAdded() || getView() == null) return;

        if (contentViews != null) {
            for (View v : contentViews) {
                if (v != null) v.setVisibility(View.GONE);
            }
        }

        if (errorView == null) {
            ViewStub stub = getView().findViewById(stubId);
            if (stub != null) {
                errorView = stub.inflate();
            }
        }

        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);

            TextView tvErrorMsg = errorView.findViewById(R.id.tvErrorMsg);
            if (tvErrorMsg != null && message != null) {
                tvErrorMsg.setText(message);
            }

            Button btnTryAgain = errorView.findViewById(R.id.btnTryAgain);
            if (btnTryAgain != null && tryAgainListener != null) {
                btnTryAgain.setOnClickListener(v -> tryAgainListener.onTryAgain());
            }
        }
    }

    protected void hideErrorState(@Nullable View[] contentViews) {
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }

        if (contentViews != null) {
            for (View v : contentViews) {
                if (v != null) v.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void showLoading(@Nullable ProgressBar progressBar, @Nullable View[] contentViews) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        hideErrorState(null);
        if (contentViews != null) {
            for (View v : contentViews) {
                if (v != null) v.setVisibility(View.GONE);
            }
        }
    }

    protected void hideLoading(@Nullable ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}

