package com.koby.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.koby.myapplication.model.Cocktail;
import com.koby.myapplication.repository.CocktailRepository;
import com.koby.myapplication.util.Resource;

import java.util.List;

public class CocktailViewModel extends AndroidViewModel {

    //Vars
    private CocktailRepository cocktailRepository;
    private LiveData<Resource<List<Cocktail>>> popularResult;
    private LiveData<Resource<List<Cocktail>>> searchResult;

    //Constructor
    public CocktailViewModel(@NonNull Application application) {
        super(application);
        this.cocktailRepository = CocktailRepository.getInstance(application);
        this.popularResult = cocktailRepository.getPopularCocktails();
    }

    //Get popular cocktails
    public LiveData<Resource<List<Cocktail>>> getPopularCocktails() {
        return popularResult;
    }

    //Get favorite cocktails
    public LiveData<Resource<List<Cocktail>>> getFavoriteCocktails() {
        return cocktailRepository.getFavoriteCocktails();
    }

    //Get searched cocktails
    public LiveData<Resource<List<Cocktail>>> getSearchedCocktail(String query) {
        searchResult = cocktailRepository.getSearchedCocktails(query);
        return searchResult;
    }

    public LiveData<Resource<List<Cocktail>>> getSearchedCocktail() {
        return searchResult;
    }

    public void update(Cocktail cocktail) {
        cocktailRepository.updateCocktail(cocktail);
    }


}
