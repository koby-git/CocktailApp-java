package com.koby.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CocktailResponse {

    @SerializedName("drinks")
    List<Cocktail> cocktails;

    public List<Cocktail> getCocktails() {
        return cocktails;
    }
}
