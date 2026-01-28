package com.example.cheffy.ui.onboarding.presenter;

import com.example.cheffy.ui.onboarding.models.OnboardingItem;

import java.util.List;

public interface OnboardingContract {
    interface View {
        void setupViewPager(List<OnboardingItem> items);
        void scrollToNextItem();
        void navigateToWelcomeScreen();
        void setButtonIcon(int iconResId);
    }

    interface Presenter {
        void attachView(OnboardingContract.View view);
        void loadOnboardingItems();
        void onNextClicked(int currentItem, int itemCount);
        void onPageChanged(int currentItem, int itemCount);
        void onOnboardingComplete();
        void detachView();
    }
}
