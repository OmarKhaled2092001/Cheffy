package com.example.cheffy.data.meals.datasource.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;

@Database(entities = {FavoriteMealEntity.class}, version = 1, exportSchema = false)
public abstract class MealsDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "cheffy_database";
    private static volatile MealsDatabase instance;

    public abstract FavoriteMealDao favoriteMealDao();

    public static synchronized MealsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MealsDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }
}
