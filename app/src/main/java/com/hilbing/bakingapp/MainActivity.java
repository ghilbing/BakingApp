package com.hilbing.bakingapp;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hilbing.bakingapp.adapter.RecipeAdapter;
import com.hilbing.bakingapp.apiConnection.ApiInterface;
import com.hilbing.bakingapp.apiConnection.service.ApiServiceGenerator;
import com.hilbing.bakingapp.fragment.RecipeFragment;
import com.hilbing.bakingapp.model.Recipe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

//    private List<Recipe> recipes = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

        RecipeFragment fragment = new RecipeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.recipe_fragment, fragment);
        transaction.commit();


    }

//        setRecyclerView();
//
//        loadData();
//    }

//    private void setRecyclerView() {
//        mRecyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        mRecyclerView.setLayoutManager(layoutManager);
//
//    }
//
//    private void loadData(){
//        ApiInterface apiInterface = ApiServiceGenerator.createService(ApiInterface.class);
//        Call<JsonArray> call = apiInterface.fetchRecipes();
//        call.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                if (response.isSuccessful()){
//                    if (response.body() != null){
//                        Log.d(TAG, response.body().toString());
//                        String data = response.body().toString();
//
//                        Type type = new TypeToken<List<Recipe>>(){}.getType();
//                        recipes = getListRecipesFromJson(data, type);
//
//                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                        mRecyclerView.setAdapter(new RecipeAdapter(getApplicationContext(), recipes));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//                Log.e(TAG, t.getMessage());
//                Toast.makeText(MainActivity.this, getResources().getString(R.string.error_fetching_data), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private static <T> List<T> getListRecipesFromJson(String data, Type type) {
//        if (!isValid(data)){
//            return null;
//        }
//        return new Gson().fromJson(data, type);
//
//
//    }
//
//    private static boolean isValid(String data) {
//        try{
//            new JsonParser().parse(data);
//            return true;
//        }catch (JsonSyntaxException e){
//            return false;
//        }
//    }

}
