package com.example.cheffy.ui.onboarding.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.cheffy.R;
import com.example.cheffy.data.local.AppPreferences;
import com.example.cheffy.ui.onboarding.presenter.OnboardingContract;
import com.example.cheffy.data.onboarding.models.OnboardingItem;
import com.example.cheffy.ui.onboarding.presenter.OnboardingPresenter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class OnboardingFragment extends Fragment implements OnboardingContract.View {
    private OnboardingAdapter adapter;
    private ViewPager2 viewPager2;
    private TabLayout tabIndicator;
    private ImageButton btnNext;
    private OnboardingContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppPreferences appPreferences = new AppPreferences(requireContext());
        presenter = new OnboardingPresenter(this, appPreferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        initViews(view);

        presenter.loadOnboardingItems();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (presenter != null && adapter != null) {
                    presenter.onPageChanged(position, adapter.getItemCount());
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter != null && viewPager2 != null) {
                    presenter.onNextClicked(viewPager2.getCurrentItem(), adapter.getItemCount());
                }
            }
        });

    }

    private void initViews(View view) {
        viewPager2 = view.findViewById(R.id.viewPager);
        tabIndicator = view.findViewById(R.id.tabIndicator);
        btnNext = view.findViewById(R.id.btnNext);
    }

    @Override
    public void setupViewPager(List<OnboardingItem> items) {
        adapter = new OnboardingAdapter(items);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabIndicator, viewPager2, (tab, position) -> {}).attach();
    }

    @Override
    public void scrollToNextItem() {
        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
    }

    @Override
    public void navigateToWelcomeScreen() {
        if (isAdded() && getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_onboardingFragment_to_welcomeFragment);
        }
    }

    @Override
    public void setButtonIcon(int iconResId) {
        btnNext.setImageResource(iconResId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}