package com.example.cheffy.ui.onboarding.presenter;

import com.example.cheffy.R;
import com.example.cheffy.data.local.AppPreferences;
import com.example.cheffy.ui.onboarding.model.OnboardingItem;

import java.util.ArrayList;
import java.util.List;

public class OnboardingPresenter implements OnboardingContract.Presenter {

    private OnboardingContract.View view;
    private final AppPreferences appPreferences;

    public OnboardingPresenter(AppPreferences appPreferences) {
        this.appPreferences = appPreferences;
    }

    @Override
    public void attachView(OnboardingContract.View view) {
        this.view = view;
    }

    @Override
    public void loadOnboardingItems() {
        List<OnboardingItem> items = new ArrayList<>();

        items.add(new OnboardingItem(
                R.drawable.onboarding_plan,
                R.string.onboarding_1_title_part1,
                R.string.onboarding_1_title_part2,
                R.string.onboarding_1_description
        ));

        items.add(new OnboardingItem(
                R.drawable.onboarding_discover,
                R.string.onboarding_2_title_part1,
                R.string.onboarding_2_title_part2,
                R.string.onboarding_2_description
        ));

        items.add(new OnboardingItem(
                R.drawable.onboarding_offline,
                R.string.onboarding_3_title_part1,
                R.string.onboarding_3_title_part2,
                R.string.onboarding_3_description
        ));

        if (view != null) view.setupViewPager(items);
    }

    @Override
    public void onNextClicked(int currentItem, int itemCount) {
        if (view == null) return;
        if (currentItem < itemCount - 1) {
            view.scrollToNextItem();
        } else {
            onOnboardingComplete();
        }
    }

    @Override
    public void onPageChanged(int currentItem, int itemCount) {
        if (view == null) return;

        if (currentItem == itemCount - 1) {
            view.setButtonIcon(R.drawable.check);
        } else {
            view.setButtonIcon(R.drawable.ic_next_arrow);
        }
    }

    @Override
    public void onOnboardingComplete() {
        appPreferences.setFirstRunComplete();
        if (view != null) {
            view.navigateToWelcomeScreen();
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}

