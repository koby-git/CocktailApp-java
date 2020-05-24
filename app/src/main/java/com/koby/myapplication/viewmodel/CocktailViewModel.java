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

    private static final String TAG = "CocktailViewModel";

    //Vars
    private CocktailRepository cocktailRepository;
    private LiveData<Resource<List<Cocktail>>> result;

    //Constructor
    public CocktailViewModel(@NonNull Application application) {
        super(application);
        this.cocktailRepository = CocktailRepository.getInstance(application);
        this.result = cocktailRepository.getPopularCocktails();
    }


    //Get popular cocktails
    public LiveData<Resource<List<Cocktail>>> getPopularCocktails() {
        Log.d(TAG, "getPopularCocktails: call");
        return result;
    }

    //Get favorite cocktails
    public LiveData<Resource<List<Cocktail>>> getFavoriteCocktails() {
        return cocktailRepository.getFavoriteCocktails();
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

    public void update(Cocktail cocktail) {
        cocktailRepository.updateCocktail(cocktail);
    }


}
