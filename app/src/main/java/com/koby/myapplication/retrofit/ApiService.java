package com.koby.myapplication.retrofit;

import com.koby.myapplication.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 2; // 2 seconds
    public static final int WRITE_TIMEOUT = 2; // 2 seconds
    public static final String BASE_URL = "https://www.thecocktaildb.com/api/json/v2/1/";

    private static OkHttpClient client = new OkHttpClient.Builder()

            // establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            .retryOnConnectionFailure(false)

            .build();


    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());


    private static Retrofit retrofit = retrofitBuilder.build();

    private static ApiRequest apiRequest = retrofit.create(ApiRequest.class);

    public static ApiRequest getApiRequest(){return  apiRequest;}
}
