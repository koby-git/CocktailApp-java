package com.koby.myapplication.util;

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

    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();

    public NetworkBoundResource() {
        init();
    }

    public void init(){

        LiveData<CacheObject> dbSource = loadFromDb();

        results.addSource(dbSource, cacheObject -> {
            System.out.println("1111");
            System.out.println(cacheObject);
            if (cacheObject==null) {
                System.out.println("2222");
                fetchFromNetwork();
            }else {
                System.out.println("3333");
                results.setValue(Resource.success(cacheObject));
            }
        });




    }

    private void fetchFromNetwork() {

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();

        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(ApiResponse<RequestObject> requestObjectApiResponse) {
                saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse)requestObjectApiResponse));


                /*
                    3 cases:
                       1) ApiSuccessResponse
                       2) ApiErrorResponse
                       3) ApiEmptyResponse
                 */

                if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {
//                    Log.d(TAG, "onChanged: ApiSuccessResponse.");

                    AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            System.out.println("5555");
                            // save the response to the local db
                            saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse));
                        }
                    });
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {

                } else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
//                    Log.d(TAG, "onChanged: ApiErrorResponse.");
//                    results.addSource(dbSource, new Observer<CacheObject>() {
//                        @Override
//                        public void onChanged(@Nullable CacheObject cacheObject) {
//                            setValue(
//                                    Resource.error(
//                                            ((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
//                                            cacheObject
//                                    )
//                            );
//                        }
//                    });
                }
            }
        });

    }


    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response){
        return (CacheObject) response.getBody();
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