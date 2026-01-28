package com.example.cheffy.data.meals.models;

import com.google.gson.annotations.SerializedName;

public class Area {

    @SerializedName("strArea")
    private final String name;

    public Area(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getFlagUrl() {
        if (name == null || name.isEmpty()) return null;

        return "https://www.themealdb.com/images/icons/flags/big/64/" + 
               getCountryCode(name) + ".png";
    }

    private String getCountryCode(String areaName) {
        if (areaName == null) return "unknown";

        switch (areaName.toLowerCase()) {
            case "algerian": return "dz";
            case "american": return "us";
            case "argentinian": return "ar";
            case "australian": return "au";
            case "british": return "gb";
            case "canadian": return "ca";
            case "chinese": return "cn";
            case "croatian": return "hr";
            case "dutch": return "nl";
            case "egyptian": return "eg";
            case "filipino": return "ph";
            case "french": return "fr";
            case "greek": return "gr";
            case "indian": return "in";
            case "irish": return "ie";
            case "italian": return "it";
            case "jamaican": return "jm";
            case "japanese": return "jp";
            case "kenyan": return "ke";
            case "malaysian": return "my";
            case "mexican": return "mx";
            case "moroccan": return "ma";
            case "norwegian": return "no";
            case "polish": return "pl";
            case "portuguese": return "pt";
            case "russian": return "ru";
            case "saudi arabian": return "sa";
            case "slovakian": return "sk";
            case "spanish": return "es";
            case "syrian": return "sy";
            case "thai": return "th";
            case "tunisian": return "tn";
            case "turkish": return "tr";
            case "ukrainian": return "ua";
            case "uruguayan": return "uy";
            case "venezulan": return "ve";
            case "vietnamese": return "vn";
            default: return "unknown";
        }
    }
}
