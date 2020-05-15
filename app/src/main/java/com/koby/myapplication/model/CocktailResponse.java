package com.koby.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CocktailResponse {

    //TODO: CHANGE TO DRENKS
    @SerializedName("drinks")
    List<Cocktail> cocktails;

    public List<Cocktail> getCocktails() {
        return cocktails;
    }
}
