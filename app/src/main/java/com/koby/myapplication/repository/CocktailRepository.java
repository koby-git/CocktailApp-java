package com.koby.myapplication.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.koby.myapplication.R;
import com.koby.myapplication.db.CocktailDao;
import com.koby.myapplication.db.CocktailDatabase;
import com.koby.myapplication.model.Cocktail;
import com.koby.myapplication.model.CocktailResponse;
import com.koby.myapplication.retrofit.ApiResponse;
import com.koby.myapplication.retrofit.ApiService;
import com.koby.myapplication.util.AppExecutor;
import com.koby.myapplication.util.Resource;

import java.util.List;

public class CocktailRepository {

    //Vars
    public static final String TAG = "CocktailRepository";
    private CocktailDao cocktailDao;

    //Singleton
    private static CocktailRepository ourInstance;
    public static CocktailRepository getInstance(Context context) {
        if (ourInstance == null) {
            return new CocktailRepository(context);
        }
        return ourInstance;
    }
    private CocktailRepository(Context context) {
        cocktailDao = CocktailDatabase.getInstance(context).getCocktailDao();

        //Delete table
//        AppExecutor.getInstance().getDiskIO().execute(() -> cocktailDao.deleteAll());
    }

    //Get popular cocktails from retrofit & cache
    public LiveData<Resource<List<Cocktail>>> getPopularCocktails() {

        MediatorLiveData<Resource<List<Cocktail>>> results = new MediatorLiveData<>();
        LiveData<List<Cocktail>> dbSource = cocktailDao.getPopularCocktails();

        results.addSource(dbSource, cocktails -> {
            if (cocktails.isEmpty()) {

                //This is the problem
//                results.setValue(Resource.loading(cocktails));
            } else {
                results.setValue(Resource.success(cocktails));
            }
        });

        LiveData<ApiResponse<CocktailResponse>> apiResponse = ApiService.getApiRequest().getPopularCocktail();

        results.addSource(apiResponse, cocktailResponseApiResponse -> {
            results.removeSource(apiResponse);
            results.removeSource(dbSource);

            if (cocktailResponseApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                CocktailResponse cocktailResponse =
                        (CocktailResponse) ((ApiResponse.ApiSuccessResponse) cocktailResponseApiResponse).getBody();



                saveCallResult(cocktailResponse);

                //Dosen't fix the problem
                results.addSource(dbSource,cocktails -> {
                    results.setValue(Resource.success(cocktails));
                });
                
            } else if (cocktailResponseApiResponse instanceof ApiResponse.ApiEmptyResponse) {
            } else if (cocktailResponseApiResponse instanceof ApiResponse.ApiErrorResponse) {
            }
        });

        return results;
    }

    //Get favorite cocktails from cache
    public LiveData<Resource<List<Cocktail>>> getFavoriteCocktails() {
        MediatorLiveData<Resource<List<Cocktail>>> results = new MediatorLiveData<>();
        LiveData<List<Cocktail>> dbSource = cocktailDao.getFavoriteCocktails();

        results.addSource(dbSource, cocktails -> {
            results.removeSource(dbSource);
            results.setValue(Resource.success(cocktails));
        });
        return results;
    }

    //Get searched cocktails from retrofit & cache
    public LiveData<Resource<List<Cocktail>>> getSearchedCocktails(String s) {

        MediatorLiveData<Resource<List<Cocktail>>> results = new MediatorLiveData<>();
        final LiveData<ApiResponse<CocktailResponse>> apiResponse = ApiService.getApiRequest().getCocktails(s);

        results.addSource(apiResponse, cocktailResponseApiResponse -> {
            if (cocktailResponseApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                CocktailResponse cocktailResponse =
                        (CocktailResponse) ((ApiResponse.ApiSuccessResponse) cocktailResponseApiResponse).getBody();

                AppExecutor.getInstance().getDiskIO().execute(() -> {

                    for (Cocktail cocktail : cocktailResponse.getCocktails()){
                        Cocktail cacheCocktail = cocktailDao.getCocktail(cocktail.getId()+"");

                        if (cacheCocktail != null){
                            if (cacheCocktail.isFavorite()) {
                                cocktail.setFavorite(true);
                            }
                            if (cacheCocktail.isPopular()){
                                cocktail.setPopular(true);
                            }
                        }
                    }
                });
                
                results.setValue(Resource.success(cocktailResponse.getCocktails()));
            } else if (cocktailResponseApiResponse instanceof ApiResponse.ApiEmptyResponse) {
            } else if (cocktailResponseApiResponse instanceof ApiResponse.ApiErrorResponse) { }
        });
        return results;
    }



    //CRUD
    public void insertCocktail(Cocktail cocktail) {
        AppExecutor.getInstance().getDiskIO().execute(() ->
                cocktailDao.insert(cocktail));
    }
    public void updateCocktail(Cocktail cocktail) {
        AppExecutor.getInstance().getDiskIO().execute(() -> {
            int i = cocktailDao.update(cocktail);
            //if is not in database then insert
            if (i == 0){
                cocktailDao.insert(cocktail);
            }
        });
    }

    //Set cocktails to cache
    private void saveCallResult(@NonNull CocktailResponse item) {

        AppExecutor.getInstance().getDiskIO().execute(() -> {

            if (item.getCocktails() != null) { // recipe list will be null if the api key is expired
                Cocktail[] cocktails = new Cocktail[item.getCocktails().size()];
                int index = 0;
                for (long rowid : cocktailDao.insert((Cocktail[]) (item.getCocktails().toArray(cocktails)))) {
                    if (rowid == -1) {
                        Log.d(TAG, "saveCallResult: CONFLICT... This recipe is already in the cache");
                        // if the recipe already exists... I don't want to set the ingredients or timestamp b/c
                        // they will be erased

                        cocktailDao.update(cocktails[index].getId()+"",
                                cocktails[index].getName(),
                                cocktails[index].getImageUri(),
                                cocktails[index].getInstruction());

                        index++;
                    }
                }
            }
        });
    }
}
