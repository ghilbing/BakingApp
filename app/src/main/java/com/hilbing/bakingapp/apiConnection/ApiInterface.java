package com.hilbing.bakingapp.apiConnection;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    String END_POINT = "baking.json";

    @GET(END_POINT)
    Call<JsonArray> fetchRecipes();

}
