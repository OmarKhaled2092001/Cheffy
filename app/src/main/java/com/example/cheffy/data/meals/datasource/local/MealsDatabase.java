package com.example.cheffy.data.meals.datasource.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;
import com.example.cheffy.data.meals.models.local.MealPlanEntity;

@Database(entities = {FavoriteMealEntity.class, MealPlanEntity.class}, version = 2, exportSchema = false)
public abstract class MealsDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "cheffy_database";
    private static volatile MealsDatabase instance;

    public abstract FavoriteMealDao favoriteMealDao();
    public abstract MealPlanDao mealPlanDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `meal_plans` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`id_meal` TEXT, " +
                "`day_of_week` TEXT, " +
                "`name` TEXT, " +
                "`category` TEXT, " +
                "`area` TEXT, " +
                "`instructions` TEXT, " +
                "`thumbnail` TEXT, " +
                "`youtube` TEXT, " +
                "`tags` TEXT, " +
                "`ingredient1` TEXT, `ingredient2` TEXT, `ingredient3` TEXT, " +
                "`ingredient4` TEXT, `ingredient5` TEXT, `ingredient6` TEXT, " +
                "`ingredient7` TEXT, `ingredient8` TEXT, `ingredient9` TEXT, " +
                "`ingredient10` TEXT, `ingredient11` TEXT, `ingredient12` TEXT, " +
                "`ingredient13` TEXT, `ingredient14` TEXT, `ingredient15` TEXT, " +
                "`ingredient16` TEXT, `ingredient17` TEXT, `ingredient18` TEXT, " +
                "`ingredient19` TEXT, `ingredient20` TEXT, " +
                "`measure1` TEXT, `measure2` TEXT, `measure3` TEXT, " +
                "`measure4` TEXT, `measure5` TEXT, `measure6` TEXT, " +
                "`measure7` TEXT, `measure8` TEXT, `measure9` TEXT, " +
                "`measure10` TEXT, `measure11` TEXT, `measure12` TEXT, " +
                "`measure13` TEXT, `measure14` TEXT, `measure15` TEXT, " +
                "`measure16` TEXT, `measure17` TEXT, `measure18` TEXT, " +
                "`measure19` TEXT, `measure20` TEXT, " +
                "`added_at` INTEGER NOT NULL DEFAULT 0)"
            );
        }
    };

    public static synchronized MealsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MealsDatabase.class,
                    DATABASE_NAME
            )
            .addMigrations(MIGRATION_1_2)
            .build();
        }
        return instance;
    }
}
