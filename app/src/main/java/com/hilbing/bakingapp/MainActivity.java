package com.hilbing.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonArray;
import com.hilbing.bakingapp.apiConnection.ApiInterface;
import com.hilbing.bakingapp.apiConnection.service.ApiServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
    }

    private void loadData(){
        ApiInterface apiInterface = ApiServiceGenerator.createService(ApiInterface.class);
        Call<JsonArray> call = apiInterface.fetchRecipes();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.d(TAG, response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

}
