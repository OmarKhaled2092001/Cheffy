package com.example.cheffy;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private boolean shouldShowBottomNav = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());

            boolean isKeyboardVisible = imeInsets.bottom > 0;

            if (isKeyboardVisible) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (shouldShowBottomNav) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();

                if (id == R.id.splashFragment
                        || id == R.id.onboardingFragment
                        || id == R.id.welcomeFragment
                        || id == R.id.loginFragment
                        || id == R.id.signupFragment
                        || id == R.id.forgotPasswordFragment
                        || id == R.id.mealDetailsFragment
                        || id == R.id.mealsListFragment) {
                    shouldShowBottomNav = false;
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    shouldShowBottomNav = true;
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}