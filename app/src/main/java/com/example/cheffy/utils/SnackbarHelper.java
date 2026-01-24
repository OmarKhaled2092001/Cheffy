package com.example.cheffy.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.cheffy.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarHelper {

    public static void showError(View view, String message) {
        show(view, message, R.color.snack_error_bg, R.drawable.error);
    }

    public static void showSuccess(View view, String message) {
        show(view, message, R.color.snack_success_bg, R.drawable.check);
    }

    private static void show(View rootView, String message, @ColorRes int bgColorResId, @DrawableRes int iconResId) {
        if (rootView == null) return;
        Context context = rootView.getContext();

        Snackbar snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG);

        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(Color.TRANSPARENT);
        layout.setPadding(0, 0, 0, 0);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(30, 0, 30, 30);
        layout.setLayoutParams(params);

        View customView = LayoutInflater.from(context).inflate(R.layout.view_custom_snackbar, null);
        CardView cardView = customView.findViewById(R.id.snack_card_root);
        ImageView iconView = customView.findViewById(R.id.snack_icon);
        TextView textView = customView.findViewById(R.id.snack_message);

        cardView.setCardBackgroundColor(ContextCompat.getColor(context, bgColorResId));
        iconView.setImageResource(iconResId);
        textView.setText(message);

        layout.addView(customView, 0);
        snackbar.show();
    }
}