package com.example.cheffy.data.meals.models.local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;


@Entity(tableName = "favorite_meals")
public class FavoriteMealEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_meal")
    private String idMeal;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "area")
    private String area;

    @ColumnInfo(name = "instructions")
    private String instructions;

    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    @ColumnInfo(name = "youtube")
    private String youtube;

    @ColumnInfo(name = "tags")
    private String tags;


    @ColumnInfo(name = "ingredient1") private String ingredient1;
    @ColumnInfo(name = "ingredient2") private String ingredient2;
    @ColumnInfo(name = "ingredient3") private String ingredient3;
    @ColumnInfo(name = "ingredient4") private String ingredient4;
    @ColumnInfo(name = "ingredient5") private String ingredient5;
    @ColumnInfo(name = "ingredient6") private String ingredient6;
    @ColumnInfo(name = "ingredient7") private String ingredient7;
    @ColumnInfo(name = "ingredient8") private String ingredient8;
    @ColumnInfo(name = "ingredient9") private String ingredient9;
    @ColumnInfo(name = "ingredient10") private String ingredient10;
    @ColumnInfo(name = "ingredient11") private String ingredient11;
    @ColumnInfo(name = "ingredient12") private String ingredient12;
    @ColumnInfo(name = "ingredient13") private String ingredient13;
    @ColumnInfo(name = "ingredient14") private String ingredient14;
    @ColumnInfo(name = "ingredient15") private String ingredient15;
    @ColumnInfo(name = "ingredient16") private String ingredient16;
    @ColumnInfo(name = "ingredient17") private String ingredient17;
    @ColumnInfo(name = "ingredient18") private String ingredient18;
    @ColumnInfo(name = "ingredient19") private String ingredient19;
    @ColumnInfo(name = "ingredient20") private String ingredient20;


    @ColumnInfo(name = "measure1") private String measure1;
    @ColumnInfo(name = "measure2") private String measure2;
    @ColumnInfo(name = "measure3") private String measure3;
    @ColumnInfo(name = "measure4") private String measure4;
    @ColumnInfo(name = "measure5") private String measure5;
    @ColumnInfo(name = "measure6") private String measure6;
    @ColumnInfo(name = "measure7") private String measure7;
    @ColumnInfo(name = "measure8") private String measure8;
    @ColumnInfo(name = "measure9") private String measure9;
    @ColumnInfo(name = "measure10") private String measure10;
    @ColumnInfo(name = "measure11") private String measure11;
    @ColumnInfo(name = "measure12") private String measure12;
    @ColumnInfo(name = "measure13") private String measure13;
    @ColumnInfo(name = "measure14") private String measure14;
    @ColumnInfo(name = "measure15") private String measure15;
    @ColumnInfo(name = "measure16") private String measure16;
    @ColumnInfo(name = "measure17") private String measure17;
    @ColumnInfo(name = "measure18") private String measure18;
    @ColumnInfo(name = "measure19") private String measure19;
    @ColumnInfo(name = "measure20") private String measure20;

    @ColumnInfo(name = "added_at")
    private long addedAt;

    public FavoriteMealEntity() {
        this.idMeal = "";
    }

    @NonNull
    public String getIdMeal() { return idMeal; }
    public void setIdMeal(@NonNull String idMeal) { this.idMeal = idMeal; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getYoutube() { return youtube; }
    public void setYoutube(String youtube) { this.youtube = youtube; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getIngredient1() { return ingredient1; }
    public void setIngredient1(String ingredient1) { this.ingredient1 = ingredient1; }
    public String getIngredient2() { return ingredient2; }
    public void setIngredient2(String ingredient2) { this.ingredient2 = ingredient2; }
    public String getIngredient3() { return ingredient3; }
    public void setIngredient3(String ingredient3) { this.ingredient3 = ingredient3; }
    public String getIngredient4() { return ingredient4; }
    public void setIngredient4(String ingredient4) { this.ingredient4 = ingredient4; }
    public String getIngredient5() { return ingredient5; }
    public void setIngredient5(String ingredient5) { this.ingredient5 = ingredient5; }
    public String getIngredient6() { return ingredient6; }
    public void setIngredient6(String ingredient6) { this.ingredient6 = ingredient6; }
    public String getIngredient7() { return ingredient7; }
    public void setIngredient7(String ingredient7) { this.ingredient7 = ingredient7; }
    public String getIngredient8() { return ingredient8; }
    public void setIngredient8(String ingredient8) { this.ingredient8 = ingredient8; }
    public String getIngredient9() { return ingredient9; }
    public void setIngredient9(String ingredient9) { this.ingredient9 = ingredient9; }
    public String getIngredient10() { return ingredient10; }
    public void setIngredient10(String ingredient10) { this.ingredient10 = ingredient10; }
    public String getIngredient11() { return ingredient11; }
    public void setIngredient11(String ingredient11) { this.ingredient11 = ingredient11; }
    public String getIngredient12() { return ingredient12; }
    public void setIngredient12(String ingredient12) { this.ingredient12 = ingredient12; }
    public String getIngredient13() { return ingredient13; }
    public void setIngredient13(String ingredient13) { this.ingredient13 = ingredient13; }
    public String getIngredient14() { return ingredient14; }
    public void setIngredient14(String ingredient14) { this.ingredient14 = ingredient14; }
    public String getIngredient15() { return ingredient15; }
    public void setIngredient15(String ingredient15) { this.ingredient15 = ingredient15; }
    public String getIngredient16() { return ingredient16; }
    public void setIngredient16(String ingredient16) { this.ingredient16 = ingredient16; }
    public String getIngredient17() { return ingredient17; }
    public void setIngredient17(String ingredient17) { this.ingredient17 = ingredient17; }
    public String getIngredient18() { return ingredient18; }
    public void setIngredient18(String ingredient18) { this.ingredient18 = ingredient18; }
    public String getIngredient19() { return ingredient19; }
    public void setIngredient19(String ingredient19) { this.ingredient19 = ingredient19; }
    public String getIngredient20() { return ingredient20; }
    public void setIngredient20(String ingredient20) { this.ingredient20 = ingredient20; }

    public String getMeasure1() { return measure1; }
    public void setMeasure1(String measure1) { this.measure1 = measure1; }
    public String getMeasure2() { return measure2; }
    public void setMeasure2(String measure2) { this.measure2 = measure2; }
    public String getMeasure3() { return measure3; }
    public void setMeasure3(String measure3) { this.measure3 = measure3; }
    public String getMeasure4() { return measure4; }
    public void setMeasure4(String measure4) { this.measure4 = measure4; }
    public String getMeasure5() { return measure5; }
    public void setMeasure5(String measure5) { this.measure5 = measure5; }
    public String getMeasure6() { return measure6; }
    public void setMeasure6(String measure6) { this.measure6 = measure6; }
    public String getMeasure7() { return measure7; }
    public void setMeasure7(String measure7) { this.measure7 = measure7; }
    public String getMeasure8() { return measure8; }
    public void setMeasure8(String measure8) { this.measure8 = measure8; }
    public String getMeasure9() { return measure9; }
    public void setMeasure9(String measure9) { this.measure9 = measure9; }
    public String getMeasure10() { return measure10; }
    public void setMeasure10(String measure10) { this.measure10 = measure10; }
    public String getMeasure11() { return measure11; }
    public void setMeasure11(String measure11) { this.measure11 = measure11; }
    public String getMeasure12() { return measure12; }
    public void setMeasure12(String measure12) { this.measure12 = measure12; }
    public String getMeasure13() { return measure13; }
    public void setMeasure13(String measure13) { this.measure13 = measure13; }
    public String getMeasure14() { return measure14; }
    public void setMeasure14(String measure14) { this.measure14 = measure14; }
    public String getMeasure15() { return measure15; }
    public void setMeasure15(String measure15) { this.measure15 = measure15; }
    public String getMeasure16() { return measure16; }
    public void setMeasure16(String measure16) { this.measure16 = measure16; }
    public String getMeasure17() { return measure17; }
    public void setMeasure17(String measure17) { this.measure17 = measure17; }
    public String getMeasure18() { return measure18; }
    public void setMeasure18(String measure18) { this.measure18 = measure18; }
    public String getMeasure19() { return measure19; }
    public void setMeasure19(String measure19) { this.measure19 = measure19; }
    public String getMeasure20() { return measure20; }
    public void setMeasure20(String measure20) { this.measure20 = measure20; }

    public long getAddedAt() { return addedAt; }
    public void setAddedAt(long addedAt) { this.addedAt = addedAt; }

    public static FavoriteMealEntity fromRemoteMeal(RemoteMeal meal) {
        FavoriteMealEntity entity = new FavoriteMealEntity();
        entity.setIdMeal(meal.getIdMeal());
        entity.setName(meal.getName());
        entity.setCategory(meal.getCategory());
        entity.setArea(meal.getArea());
        entity.setInstructions(meal.getInstructions());
        entity.setThumbnail(meal.getThumbnail());
        entity.setYoutube(meal.getYoutube());
        entity.setTags(meal.getTags());
        
        entity.setIngredient1(meal.getIngredient1());
        entity.setIngredient2(meal.getIngredient2());
        entity.setIngredient3(meal.getIngredient3());
        entity.setIngredient4(meal.getIngredient4());
        entity.setIngredient5(meal.getIngredient5());
        entity.setIngredient6(meal.getIngredient6());
        entity.setIngredient7(meal.getIngredient7());
        entity.setIngredient8(meal.getIngredient8());
        entity.setIngredient9(meal.getIngredient9());
        entity.setIngredient10(meal.getIngredient10());
        entity.setIngredient11(meal.getIngredient11());
        entity.setIngredient12(meal.getIngredient12());
        entity.setIngredient13(meal.getIngredient13());
        entity.setIngredient14(meal.getIngredient14());
        entity.setIngredient15(meal.getIngredient15());
        entity.setIngredient16(meal.getIngredient16());
        entity.setIngredient17(meal.getIngredient17());
        entity.setIngredient18(meal.getIngredient18());
        entity.setIngredient19(meal.getIngredient19());
        entity.setIngredient20(meal.getIngredient20());
        
        entity.setMeasure1(meal.getMeasure1());
        entity.setMeasure2(meal.getMeasure2());
        entity.setMeasure3(meal.getMeasure3());
        entity.setMeasure4(meal.getMeasure4());
        entity.setMeasure5(meal.getMeasure5());
        entity.setMeasure6(meal.getMeasure6());
        entity.setMeasure7(meal.getMeasure7());
        entity.setMeasure8(meal.getMeasure8());
        entity.setMeasure9(meal.getMeasure9());
        entity.setMeasure10(meal.getMeasure10());
        entity.setMeasure11(meal.getMeasure11());
        entity.setMeasure12(meal.getMeasure12());
        entity.setMeasure13(meal.getMeasure13());
        entity.setMeasure14(meal.getMeasure14());
        entity.setMeasure15(meal.getMeasure15());
        entity.setMeasure16(meal.getMeasure16());
        entity.setMeasure17(meal.getMeasure17());
        entity.setMeasure18(meal.getMeasure18());
        entity.setMeasure19(meal.getMeasure19());
        entity.setMeasure20(meal.getMeasure20());
        
        entity.setAddedAt(System.currentTimeMillis());
        return entity;
    }


    public RemoteMeal toRemoteMeal() {
        RemoteMeal meal = new RemoteMeal();
        meal.idMeal = this.idMeal;
        meal.name = this.name;
        meal.category = this.category;
        meal.area = this.area;
        meal.instructions = this.instructions;
        meal.thumbnail = this.thumbnail;
        meal.youtube = this.youtube;
        meal.tags = this.tags;
        
        meal.ingredient1 = this.ingredient1;
        meal.ingredient2 = this.ingredient2;
        meal.ingredient3 = this.ingredient3;
        meal.ingredient4 = this.ingredient4;
        meal.ingredient5 = this.ingredient5;
        meal.ingredient6 = this.ingredient6;
        meal.ingredient7 = this.ingredient7;
        meal.ingredient8 = this.ingredient8;
        meal.ingredient9 = this.ingredient9;
        meal.ingredient10 = this.ingredient10;
        meal.ingredient11 = this.ingredient11;
        meal.ingredient12 = this.ingredient12;
        meal.ingredient13 = this.ingredient13;
        meal.ingredient14 = this.ingredient14;
        meal.ingredient15 = this.ingredient15;
        meal.ingredient16 = this.ingredient16;
        meal.ingredient17 = this.ingredient17;
        meal.ingredient18 = this.ingredient18;
        meal.ingredient19 = this.ingredient19;
        meal.ingredient20 = this.ingredient20;
        
        meal.measure1 = this.measure1;
        meal.measure2 = this.measure2;
        meal.measure3 = this.measure3;
        meal.measure4 = this.measure4;
        meal.measure5 = this.measure5;
        meal.measure6 = this.measure6;
        meal.measure7 = this.measure7;
        meal.measure8 = this.measure8;
        meal.measure9 = this.measure9;
        meal.measure10 = this.measure10;
        meal.measure11 = this.measure11;
        meal.measure12 = this.measure12;
        meal.measure13 = this.measure13;
        meal.measure14 = this.measure14;
        meal.measure15 = this.measure15;
        meal.measure16 = this.measure16;
        meal.measure17 = this.measure17;
        meal.measure18 = this.measure18;
        meal.measure19 = this.measure19;
        meal.measure20 = this.measure20;
        
        return meal;
    }
}
