package com.koby.myapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.koby.myapplication.model.Cocktail;
import com.koby.myapplication.repository.CocktailRepository;
import com.koby.myapplication.util.Resource;

import java.util.List;


public class CocktailViewModel extends AndroidViewModel {

    //Vars
    private CocktailRepository cocktailRepository;
    private static final String TAG = "CocktailViewModel";
    LiveData<Resource<List<Cocktail>>> result;
    //Constructor
    public CocktailViewModel(@NonNull Application application) {
        super(application);
        this.cocktailRepository = CocktailRepository.getInstance(application);
    }

    //Get popular cocktails
    public LiveData<Resource<List<Cocktail>>> getPopularCocktails() {
        result = cocktailRepository.getPopularCocktails();
        if (result.getValue() != null) {
            switch (result.getValue().status) {
                case SUCCESS:
                    return result;
                case LOADING:
                    Log.d(TAG, "getPopularCocktails: loading");
                case ERROR:
                    Log.d(TAG, "getPopularCocktails: " + result.getValue().message);
                    break;
            }
        }

        return result;
    }

    //Get favorite cocktails
    public LiveData<Resource<List<Cocktail>>> getFavoriteCocktails() {
        LiveData<Resource<List<Cocktail>>> result = cocktailRepository.getFavoriteCocktails();
        if (result.getValue() != null) {
            switch (result.getValue().status) {
                case SUCCESS:
                    Log.d(TAG, "getFavoriteCocktails: success");
                    return result;
                case LOADING:

                    Log.d(TAG, "getFavoriteCocktails: loading");
                    break;
                case ERROR:
                    Log.d(TAG, "getFavoriteCocktails: " + result.getValue().message);
                    break;
            }
        }

        return result;
    }

    //Get searched cocktails
    public LiveData<Resource<List<Cocktail>>> getSearchedCocktail(String s) {
        LiveData<Resource<List<Cocktail>>> result = cocktailRepository.getSearchedCocktails(s);
        if (result.getValue() != null) {
            switch (result.getValue().status) {
                case SUCCESS:
                    Log.d(TAG, "getSearchedCocktail: success");
                    return result;
                case LOADING:

                    Log.d(TAG, "getSearchedCocktail: loading");
                    break;
                case ERROR:
                    Log.d(TAG, "getSearchedCocktail: " + result.getValue().message);
                    break;
            }
        }
        return result;
    }

    //CRUD
    public void insert(Cocktail cocktail) {
        cocktailRepository.insertCocktail(cocktail);
    }
    public void update(Cocktail cocktail) {
        cocktailRepository.updateCocktail(cocktail);
    }


}
