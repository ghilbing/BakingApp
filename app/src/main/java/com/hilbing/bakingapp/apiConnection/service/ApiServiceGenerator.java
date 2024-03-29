package com.hilbing.bakingapp.apiConnection.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hilbing.bakingapp.BuildConfig;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceGenerator {
    public static <S> S createService(Class<S> serviceClass){
        String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

        //Enables complex map key serializations
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL);

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
