package com.koby.myapplication.retrofit;


import androidx.lifecycle.LiveData;

import com.koby.myapplication.model.CocktailResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {

    @GET("popular.php")
    LiveData<ApiResponse<CocktailResponse>> getPopularCocktail();

    @GET("search.php?")
    LiveData<ApiResponse<CocktailResponse>> getCocktails(@Query("s") String s);
}
