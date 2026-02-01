package com.example.cheffy.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;


public class ProfileFragment extends Fragment implements ProfileContract.View {

    private ImageView imgProfile, optionThemeIcon, optionLanguageIcon;
    private TextView tvUserName, tvUserEmail, tvFavCount, tvPlanCount, optionThemeTitle, optionLanguageTitle, tvLogout;
    private View optionThemeRoot, optionLanguageRoot;

    private ProfileContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenter(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        presenter.attachView(this);
        
        setupClickListeners();
        
        setupSettingsOptions();
        
        presenter.loadUserProfile();
        presenter.loadStats();
    }

    private void initViews(View view) {
        imgProfile = view.findViewById(R.id.imgProfile);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        tvFavCount = view.findViewById(R.id.tvFavCount);
        tvPlanCount = view.findViewById(R.id.tvPlanCount);

        optionThemeRoot = view.findViewById(R.id.optionTheme);
        optionLanguageRoot = view.findViewById(R.id.optionLanguage);
        
        optionThemeTitle = optionThemeRoot.findViewById(R.id.tvOptionTitle);
        optionThemeIcon = optionThemeRoot.findViewById(R.id.ivIcon);
        optionLanguageTitle = optionLanguageRoot.findViewById(R.id.tvOptionTitle);
        optionLanguageIcon = optionLanguageRoot.findViewById(R.id.ivIcon);

        tvLogout = view.findViewById(R.id.tvLogout);
    }

    private void setupClickListeners() {
        optionThemeRoot.setOnClickListener(v -> presenter.onThemeClicked());
        
        optionLanguageRoot.setOnClickListener(v -> presenter.onLanguageClicked());
        
        tvLogout.setOnClickListener(v -> presenter.onLogoutClicked());
    }

    private void setupSettingsOptions() {
        optionThemeTitle.setText(R.string.theme);
        optionThemeIcon.setImageResource(android.R.drawable.ic_menu_preferences);
        
        optionLanguageTitle.setText(R.string.language);
        optionLanguageIcon.setImageResource(android.R.drawable.ic_menu_mapmode);
    }


    @Override
    public void showUserInfo(String displayName, String email, String photoUrl) {
        String name = displayName != null && !displayName.isEmpty() ? displayName : "User";
        tvUserName.setText(name);
        
        String emailText = email != null && !email.isEmpty() ? email : "";
        tvUserEmail.setText(emailText);
        
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.ic_default_avatar)
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.ic_default_avatar);
        }
    }

    @Override
    public void showFavoriteCount(int count) {
        tvFavCount.setText(String.valueOf(count));
    }

    @Override
    public void showPlanCount(int count) {
        tvPlanCount.setText(String.valueOf(count));
    }


    @Override
    public void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void navigateToLogin() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_logout_to_welcome);
    }

    @Override
    public void showGuestMode() {
        tvLogout.setText(R.string.sign_in);
        tvLogout.setOnClickListener(v -> navigateToLogin());
    }

    @Override
    public void showLoggedInMode() {
        tvLogout.setText(R.string.logout);
        tvLogout.setOnClickListener(v -> presenter.onLogoutClicked());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
