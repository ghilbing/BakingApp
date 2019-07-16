package com.hilbing.bakingapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hilbing.bakingapp.MainActivity;
import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.activities.RecipeDetailActivity;
import com.hilbing.bakingapp.adapter.RecipeAdapter;
import com.hilbing.bakingapp.apiConnection.ApiInterface;
import com.hilbing.bakingapp.apiConnection.service.ApiServiceGenerator;
import com.hilbing.bakingapp.model.Recipe;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeClickListener {

    private static final String TAG = RecipeFragment.class.getSimpleName();

    private Context mContext;
    private RecipeAdapter mAdapter;
    private List<Recipe> recipes;

    private static final String RECIPE_KEY = "Recipe";

    @BindView(R.id.rv_recipes_fragment)
    RecyclerView mRecycler;

    public RecipeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        mContext = getActivity();

        initViews();


        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_KEY)){
            loadData();
        }

        loadData();
        return view;
    }

    private void initViews(){
        mAdapter = new RecipeAdapter(mContext, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        mRecycler.setAdapter(mAdapter);
    }

    private void loadData() {
            ApiInterface apiInterface = ApiServiceGenerator.createService(ApiInterface.class);
            Call<JsonArray> call = apiInterface.fetchRecipes();
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Log.d(TAG, response.body().toString());
                            String data = response.body().toString();


                            Type type = new TypeToken<List<Recipe>>() {
                            }.getType();
                            recipes = getListRecipesFromJson(data, type);
                            mAdapter.setData(recipes);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_fetching_data), Toast.LENGTH_SHORT).show();
                }
            });

    }

    private static <T> List<T> getListRecipesFromJson(String data, Type type) {
        if (!isValid(data)){
            return null;
        }
        return new Gson().fromJson(data, type);


    }

    private static boolean isValid(String data) {
        try{
            new JsonParser().parse(data);
            return true;
        }catch (JsonSyntaxException e){
            return false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRecipeClick(Recipe recipe){
        Intent intent = new Intent(mContext, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_KEY, recipe);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
