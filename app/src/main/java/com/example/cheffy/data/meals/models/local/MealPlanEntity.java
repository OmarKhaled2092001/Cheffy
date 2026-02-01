package com.example.cheffy.data.meals.models.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;

@Entity(tableName = "meal_plans")
public class MealPlanEntity extends BaseMealEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "id_meal")
    private String idMealField;

    @ColumnInfo(name = "day_of_week")
    private String dayOfWeek;

    public MealPlanEntity() {
        super();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getIdMealField() { return idMealField; }
    public void setIdMealField(String idMealField) { this.idMealField = idMealField; }

    @Override
    public String getIdMeal() { return idMealField; }
    
    @Override
    public void setIdMeal(String idMeal) { this.idMealField = idMeal; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public static MealPlanEntity fromRemoteMeal(RemoteMeal meal, String dayOfWeek) {
        MealPlanEntity entity = new MealPlanEntity();
        entity.copyFromRemoteMeal(meal);
        entity.setDayOfWeek(dayOfWeek);
        return entity;
    }
}
