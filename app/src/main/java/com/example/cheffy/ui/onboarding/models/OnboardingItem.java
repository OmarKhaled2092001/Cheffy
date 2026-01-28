package com.example.cheffy.ui.onboarding.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class OnboardingItem {
    @DrawableRes
    private final int imageRes;
    @StringRes
    private final int title1;
    @StringRes
    private final int title2;
    @StringRes
    private final int description;

    public OnboardingItem(@DrawableRes int imageRes, @StringRes int title1, @StringRes int title2, @StringRes int description) {
        this.imageRes = imageRes;
        this.title1 = title1;
        this.title2 = title2;
        this.description = description;
    }

    @DrawableRes
    public int getImageRes () {return imageRes;}
    @StringRes
    public int getTitle1 () {return title1;}
    @StringRes
    public int getTitle2 () {return title2;}
    @StringRes
    public int getDescription () {return description;}
}
