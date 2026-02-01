package com.example.cheffy.data.meals.models.local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;

@Entity(tableName = "favorite_meals")
public class FavoriteMealEntity extends BaseMealEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_meal")
    private String idMealPk = "";

    public FavoriteMealEntity() {
        super();
    }

    @NonNull
    public String getIdMealPk() {
        return idMealPk;
    }

    public void setIdMealPk(@NonNull String idMealPk) {
        this.idMealPk = idMealPk;
    }

    @NonNull
    @Override
    public String getIdMeal() {
        return idMealPk;
    }

    @Override
    public void setIdMeal(String idMeal) {
        this.idMealPk = idMeal != null ? idMeal : "";
    }

    public static FavoriteMealEntity fromRemoteMeal(RemoteMeal meal) {
        FavoriteMealEntity entity = new FavoriteMealEntity();
        entity.copyFromRemoteMeal(meal);
        return entity;
    }
}
