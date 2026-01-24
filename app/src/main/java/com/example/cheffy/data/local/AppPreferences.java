package com.example.cheffy.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static final String PREF_NAME = "cheffy_preferences";
    private static final String KEY_FIRST_RUN = "is_first_run";

    private final SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        this.sharedPreferences =  context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        return sharedPreferences.getBoolean(KEY_FIRST_RUN, true);
    }

    public void setFirstRunComplete() {
        sharedPreferences.edit()
                .putBoolean(KEY_FIRST_RUN, false)
                .apply();
    }
}
