package com.example.cheffy.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cheffy.common.base.BasePresenter;
import com.example.cheffy.data.auth.models.User;
import com.example.cheffy.data.auth.repository.AuthRepositoryImpl;
import com.example.cheffy.data.auth.repository.IAuthRepository;
import com.example.cheffy.data.meals.repository.IMealsRepository;
import com.example.cheffy.data.meals.repository.MealsRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfilePresenter extends BasePresenter<ProfileContract.View> implements ProfileContract.Presenter {

    private static final String TAG = "ProfilePresenter";
    private static final String PREFS_NAME = "cheffy_prefs";

    private final IAuthRepository authRepository;
    private final IMealsRepository mealsRepository;
    private final SharedPreferences prefs;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ProfilePresenter(Context context) {
        this.authRepository = AuthRepositoryImpl.getInstance();
        this.mealsRepository = MealsRepositoryImpl.getInstance(context);
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void attachView(ProfileContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        disposables.clear();
    }

    @Override
    public void loadUserProfile() {
        User user = authRepository.getCurrentUser();
        if (user != null) {
            view.showUserInfo(
                    user.getDisplayName(),
                    user.getEmail(),
                    user.getPhotoUrl()
            );
            view.showLoggedInMode();
        } else {
            view.showUserInfo("Guest", "Not logged in", null);
            view.showGuestMode();
        }
    }

    @Override
    public void loadStats() {
        disposables.add(
                mealsRepository.observeFavorites()
                        .map(list -> list != null ? list.size() : 0)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                count -> view.showFavoriteCount(count),
                                error -> {
                                    Log.e(TAG, "Error loading favorites count", error);
                                    view.showFavoriteCount(0);
                                }
                        )
        );

        String[] days = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        List<Flowable<Integer>> flowables = new ArrayList<>();

        for (String day : days) {
            flowables.add(
                    mealsRepository.observeMealPlanByDay(day)
                            .take(1)
                            .map(List::size)
            );
        }

        disposables.add(
                Flowable.zip(flowables, args -> {
                            int sum = 0;
                            for (Object arg : args) {
                                sum += (Integer) arg;
                            }
                            return sum;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                totalCount -> view.showPlanCount(totalCount),
                                error -> {
                                    Log.e(TAG, "Error loading total plan count", error);
                                    view.showPlanCount(0);
                                }
                        )
        );
    }

    @Override
    public void onLogoutClicked() {
        authRepository.signOut();

        disposables.add(
                mealsRepository.clearAllLocalData()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    prefs.edit().clear().apply();
                                    Log.d(TAG, "Logout complete - local data cleared");
                                    
                                    view.navigateToLogin();
                                },
                                error -> {
                                    Log.e(TAG, "Error clearing local data", error);
                                    prefs.edit().clear().apply();
                                    view.navigateToLogin();
                                }
                        )
        );
    }

    @Override
    public void onThemeClicked() {
        view.showToast("Coming Soon");
    }

    @Override
    public void onLanguageClicked() {
        view.showToast("Coming Soon");
    }

}
