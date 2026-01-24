package com.example.cheffy.common.base;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.cheffy.utils.SnackbarHelper;
import com.google.android.material.textfield.TextInputLayout;

public abstract class BaseFragment extends Fragment {

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
}
