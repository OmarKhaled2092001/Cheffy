package com.example.cheffy.data.meals.models.remote;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.cheffy.ui.mealdetails.model.IngredientItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RemoteMeal implements Parcelable {

    @SerializedName("idMeal")
    public String idMeal;

    @SerializedName("strMeal")
    public String name;

    @SerializedName("strCategory")
    public String category;

    @SerializedName("strArea")
    public String area;

    @SerializedName("strInstructions")
    public String instructions;

    @SerializedName("strMealThumb")
    public String thumbnail;

    @SerializedName("strYoutube")
    public String youtube;

    @SerializedName("strTags")
    public String tags;

    @SerializedName("strIngredient1")
    public String ingredient1;
    @SerializedName("strIngredient2")
    public String ingredient2;
    @SerializedName("strIngredient3")
    public String ingredient3;
    @SerializedName("strIngredient4")
    public String ingredient4;
    @SerializedName("strIngredient5")
    public String ingredient5;
    @SerializedName("strIngredient6")
    public String ingredient6;
    @SerializedName("strIngredient7")
    public String ingredient7;
    @SerializedName("strIngredient8")
    public String ingredient8;
    @SerializedName("strIngredient9")
    public String ingredient9;
    @SerializedName("strIngredient10")
    public String ingredient10;
    @SerializedName("strIngredient11")
    public String ingredient11;
    @SerializedName("strIngredient12")
    public String ingredient12;
    @SerializedName("strIngredient13")
    public String ingredient13;
    @SerializedName("strIngredient14")
    public String ingredient14;
    @SerializedName("strIngredient15")
    public String ingredient15;
    @SerializedName("strIngredient16")
    public String ingredient16;
    @SerializedName("strIngredient17")
    public String ingredient17;
    @SerializedName("strIngredient18")
    public String ingredient18;
    @SerializedName("strIngredient19")
    public String ingredient19;
    @SerializedName("strIngredient20")
    public String ingredient20;

    @SerializedName("strMeasure1")
    public String measure1;
    @SerializedName("strMeasure2")
    public String measure2;
    @SerializedName("strMeasure3")
    public String measure3;
    @SerializedName("strMeasure4")
    public String measure4;
    @SerializedName("strMeasure5")
    public String measure5;
    @SerializedName("strMeasure6")
    public String measure6;
    @SerializedName("strMeasure7")
    public String measure7;
    @SerializedName("strMeasure8")
    public String measure8;
    @SerializedName("strMeasure9")
    public String measure9;
    @SerializedName("strMeasure10")
    public String measure10;
    @SerializedName("strMeasure11")
    public String measure11;
    @SerializedName("strMeasure12")
    public String measure12;
    @SerializedName("strMeasure13")
    public String measure13;
    @SerializedName("strMeasure14")
    public String measure14;
    @SerializedName("strMeasure15")
    public String measure15;
    @SerializedName("strMeasure16")
    public String measure16;
    @SerializedName("strMeasure17")
    public String measure17;
    @SerializedName("strMeasure18")
    public String measure18;
    @SerializedName("strMeasure19")
    public String measure19;
    @SerializedName("strMeasure20")
    public String measure20;

    public RemoteMeal() {}

    protected RemoteMeal(Parcel in) {
        idMeal = in.readString();
        name = in.readString();
        category = in.readString();
        area = in.readString();
        instructions = in.readString();
        thumbnail = in.readString();
        youtube = in.readString();
        tags = in.readString();
        ingredient1 = in.readString();
        ingredient2 = in.readString();
        ingredient3 = in.readString();
        ingredient4 = in.readString();
        ingredient5 = in.readString();
        ingredient6 = in.readString();
        ingredient7 = in.readString();
        ingredient8 = in.readString();
        ingredient9 = in.readString();
        ingredient10 = in.readString();
        ingredient11 = in.readString();
        ingredient12 = in.readString();
        ingredient13 = in.readString();
        ingredient14 = in.readString();
        ingredient15 = in.readString();
        ingredient16 = in.readString();
        ingredient17 = in.readString();
        ingredient18 = in.readString();
        ingredient19 = in.readString();
        ingredient20 = in.readString();
        measure1 = in.readString();
        measure2 = in.readString();
        measure3 = in.readString();
        measure4 = in.readString();
        measure5 = in.readString();
        measure6 = in.readString();
        measure7 = in.readString();
        measure8 = in.readString();
        measure9 = in.readString();
        measure10 = in.readString();
        measure11 = in.readString();
        measure12 = in.readString();
        measure13 = in.readString();
        measure14 = in.readString();
        measure15 = in.readString();
        measure16 = in.readString();
        measure17 = in.readString();
        measure18 = in.readString();
        measure19 = in.readString();
        measure20 = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idMeal);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(area);
        dest.writeString(instructions);
        dest.writeString(thumbnail);
        dest.writeString(youtube);
        dest.writeString(tags);
        dest.writeString(ingredient1);
        dest.writeString(ingredient2);
        dest.writeString(ingredient3);
        dest.writeString(ingredient4);
        dest.writeString(ingredient5);
        dest.writeString(ingredient6);
        dest.writeString(ingredient7);
        dest.writeString(ingredient8);
        dest.writeString(ingredient9);
        dest.writeString(ingredient10);
        dest.writeString(ingredient11);
        dest.writeString(ingredient12);
        dest.writeString(ingredient13);
        dest.writeString(ingredient14);
        dest.writeString(ingredient15);
        dest.writeString(ingredient16);
        dest.writeString(ingredient17);
        dest.writeString(ingredient18);
        dest.writeString(ingredient19);
        dest.writeString(ingredient20);
        dest.writeString(measure1);
        dest.writeString(measure2);
        dest.writeString(measure3);
        dest.writeString(measure4);
        dest.writeString(measure5);
        dest.writeString(measure6);
        dest.writeString(measure7);
        dest.writeString(measure8);
        dest.writeString(measure9);
        dest.writeString(measure10);
        dest.writeString(measure11);
        dest.writeString(measure12);
        dest.writeString(measure13);
        dest.writeString(measure14);
        dest.writeString(measure15);
        dest.writeString(measure16);
        dest.writeString(measure17);
        dest.writeString(measure18);
        dest.writeString(measure19);
        dest.writeString(measure20);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RemoteMeal> CREATOR = new Creator<RemoteMeal>() {
        @Override
        public RemoteMeal createFromParcel(Parcel in) {
            return new RemoteMeal(in);
        }

        @Override
        public RemoteMeal[] newArray(int size) {
            return new RemoteMeal[size];
        }
    };

    public String getIdMeal() {
        return idMeal;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getTags() {
        return tags;
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public String getIngredient3() {
        return ingredient3;
    }

    public String getIngredient4() {
        return ingredient4;
    }

    public String getIngredient5() {
        return ingredient5;
    }

    public String getIngredient6() {
        return ingredient6;
    }

    public String getIngredient7() {
        return ingredient7;
    }

    public String getIngredient8() {
        return ingredient8;
    }

    public String getIngredient9() {
        return ingredient9;
    }

    public String getIngredient10() {
        return ingredient10;
    }

    public String getIngredient11() {
        return ingredient11;
    }

    public String getIngredient12() {
        return ingredient12;
    }

    public String getIngredient13() {
        return ingredient13;
    }

    public String getIngredient14() {
        return ingredient14;
    }

    public String getIngredient15() {
        return ingredient15;
    }

    public String getIngredient16() {
        return ingredient16;
    }

    public String getIngredient17() {
        return ingredient17;
    }

    public String getIngredient18() {
        return ingredient18;
    }

    public String getIngredient19() {
        return ingredient19;
    }

    public String getIngredient20() {
        return ingredient20;
    }

    public String getMeasure1() {
        return measure1;
    }

    public String getMeasure2() {
        return measure2;
    }

    public String getMeasure3() {
        return measure3;
    }

    public String getMeasure4() {
        return measure4;
    }

    public String getMeasure5() {
        return measure5;
    }

    public String getMeasure6() {
        return measure6;
    }

    public String getMeasure7() {
        return measure7;
    }

    public String getMeasure8() {
        return measure8;
    }

    public String getMeasure9() {
        return measure9;
    }

    public String getMeasure10() {
        return measure10;
    }

    public String getMeasure11() {
        return measure11;
    }

    public String getMeasure12() {
        return measure12;
    }

    public String getMeasure13() {
        return measure13;
    }

    public String getMeasure14() {
        return measure14;
    }

    public String getMeasure15() {
        return measure15;
    }

    public String getMeasure16() {
        return measure16;
    }

    public String getMeasure17() {
        return measure17;
    }

    public String getMeasure18() {
        return measure18;
    }

    public String getMeasure19() {
        return measure19;
    }

    public String getMeasure20() {
        return measure20;
    }

    public List<IngredientItem> getIngredientsList() {
        List<IngredientItem> ingredients = new ArrayList<>();
        String[] ingredientFields = {
                ingredient1, ingredient2, ingredient3, ingredient4, ingredient5,
                ingredient6, ingredient7, ingredient8, ingredient9, ingredient10,
                ingredient11, ingredient12, ingredient13, ingredient14, ingredient15,
                ingredient16, ingredient17, ingredient18, ingredient19, ingredient20
        };
        String[] measureFields = {
                measure1, measure2, measure3, measure4, measure5,
                measure6, measure7, measure8, measure9, measure10,
                measure11, measure12, measure13, measure14, measure15,
                measure16, measure17, measure18, measure19, measure20
        };

        for (int i = 0; i < ingredientFields.length; i++) {
            String ingredient = ingredientFields[i];
            if (ingredient != null && !ingredient.trim().isEmpty()) {
                String measure = measureFields[i] != null ? measureFields[i].trim() : "";
                ingredients.add(new IngredientItem(ingredient.trim(), measure));
            }
        }
        return ingredients;
    }
}
