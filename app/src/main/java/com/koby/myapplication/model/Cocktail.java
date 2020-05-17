package com.koby.myapplication.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "cocktail_table")
public class Cocktail implements Serializable {

    @PrimaryKey
    @SerializedName("idDrink")
    private int id;
    @SerializedName("strDrink")
    private String name;
    @SerializedName("strDrinkThumb")
    private String imageUri;
    @SerializedName("strInstructions")
    private String instruction;
    @SerializedName("strGlass")
    private String glass;
    @SerializedName("strTags")
    private String tags;

    private boolean isFavorite = false;
    private boolean isPopular = false;

    public Cocktail(int id, String name, String imageUri, String instruction,String tags,String glass) {
        this.id = id;
        this.name = name;
        this.imageUri = imageUri;
        this.instruction = instruction;
        this.tags = tags;
        this.glass = glass;
    }

    public String getGlass() {
        return glass;
    }

    public String getTags() {
        return tags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public String getInstruction() {
        return instruction;
    }

    @Override
    public String toString() {
        return "Cocktail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", isFavorite=" + isFavorite +
                ", isPopular=" + isPopular +
                ", instruction='" + instruction + '\'' +
                '}';
    }
}
