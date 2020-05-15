package com.koby.myapplication.util;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.koby.myapplication.model.CocktailResponse;
import com.koby.myapplication.retrofit.ApiResponse;

import io.reactivex.Flowable;

// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";
    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();

    public NetworkBoundResource() {
        init();
    }

    //Changes
    private void init(){

        // update LiveData for loading status
        results.setValue((Resource<CacheObject>) Resource.loading(null));

        // observe LiveData source from local db
        final LiveData<CacheObject> dbSource = loadFromDb();

        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {

                results.removeSource(dbSource);

                if(shouldFetch(cacheObject)){
                    // get data from the network
                    fetchFromNetwork(dbSource);
                }
                else{
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }

    //Changes
    private void fetchFromNetwork(final LiveData<CacheObject> dbSource) {


        Log.d(TAG, "fetchFromNetwork: called.");

        // update LiveData for loading status
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();

        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(dbSource);
                results.removeSource(apiResponse);

                /*
                    3 cases:
                       1) ApiSuccessResponse
                       2) ApiErrorResponse
                       3) ApiEmptyResponse
                 */

                if(requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse){
                    Log.d(TAG, "onChanged: ApiSuccessResponse.");

                    AppExecutor.getInstance().diskIO().execute(() -> {

                        // save the response to the local db
                        saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse)requestObjectApiResponse));

                        AppExecutor.getInstance().mainThread().execute(() ->
                                results.addSource(loadFromDb(), cacheObject ->
                                        setValue(Resource.success(cacheObject))));
                    });
                }
                else if(requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse){
                    Log.d(TAG, "onChanged: ApiEmptyResponse");
//                    appExecutors.mainThread().execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            results.addSource(loadFromDb(), new Observer<CacheObject>() {
//                                @Override
//                                public void onChanged(@Nullable CacheObject cacheObject) {
//                                    setValue(Resource.success(cacheObject));
//                                }
//                            });
//                        }
//                    });
                }
                else if(requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse){
                    Log.d(TAG, "onChanged: ApiErrorResponse.");
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(
                                    Resource.error(
                                            ((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
                                            cacheObject
                                    )
                            );
                        }
                    });
                }
            }
        });

    }


    //Old version
//        MediatorLiveData<Resource<List<Cocktail>>> results = new MediatorLiveData<>();
//        LiveData<List<Cocktail>> dbSource = cocktailDao.getPopularCocktails();
//
//        results.addSource(dbSource, cocktails -> {
//            if (cocktails.isEmpty()) {
//
//                //This is the problem
////                results.setValue(Resource.loading(cocktails));
//            } else {
//                results.setValue(Resource.success(cocktails));
//            }
//        });
//
//        LiveData<ApiResponse<CocktailResponse>> apiResponse = ApiService.getApiRequest().getPopularCocktail();
//
//        results.addSource(apiResponse, cocktailResponseApiResponse -> {
//            results.removeSource(apiResponse);
//            results.removeSource(dbSource);
//
//            if (cocktailResponseApiResponse instanceof ApiResponse.ApiSuccessResponse) {
//                CocktailResponse cocktailResponse =
//                        (CocktailResponse) ((ApiResponse.ApiSuccessResponse) cocktailResponseApiResponse).getBody();
//
//
//
//                saveCallResult(cocktailResponse);
//
//                //Dosen't fix the problem
//                results.addSource(dbSource,cocktails -> {
//                    results.setValue(Resource.success(cocktails));
//                });
//
//            } else if (cocktailResponseApiResponse instanceof ApiResponse.ApiEmptyResponse) {
//            } else if (cocktailResponseApiResponse instanceof ApiResponse.ApiErrorResponse) {
//            }
//        });
//
//        return results;







    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response){
        return (CacheObject) response.getBody();
    }

    private void setValue(Resource<CacheObject> newValue){
        if(results.getValue() != newValue){
            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @NonNull @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData(){
        return results;
    };
}