package com.hilbing.bakingapp.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.adapter.IngredientsAndStepsAdapter;
import com.hilbing.bakingapp.fragment.RecipeStepFragment;
import com.hilbing.bakingapp.model.Ingredient;
import com.hilbing.bakingapp.model.Recipe;
import com.hilbing.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements IngredientsAndStepsAdapter.StepClickListener, RecipeStepFragment.OnStepClickListener {

    @BindView(R.id.rv_detail)
    RecyclerView recyclerViewDetail;
    private boolean mTwoPane;
    public Recipe recipe;
    private IngredientsAndStepsAdapter mAdapter;
    public List<Ingredient> ingredients;
    public List<Step> steps;
    public String recipeName;
    public ArrayList<Object> recipeObjects;
    public static final String EXTRA = "Recipe";
    public static final String EXTRA_NAME = "Name";
    public int recipeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recipeObjects = new ArrayList<Object>();


        //get intent extras
        Intent getIntent = getIntent();

        if (getIntent == null){
            finish();
        }

        if (getIntent.hasExtra(EXTRA)){

            recipe = getIntent().getParcelableExtra(EXTRA);
            recipeId = recipe.getId();
            ingredients = recipe.getIngredients();
            steps = recipe.getSteps();
            recipeName = recipe.getName();
            recipeObjects.addAll(ingredients);
            recipeObjects.addAll(steps);
            setTitle(recipeName);
        } else {
            Toast.makeText(this, R.string.data_no_available, Toast.LENGTH_LONG).show();
        }

        if (findViewById(R.id.item_detail_container) != null){
            //For larger screens
            mTwoPane = true;
        }

        setupRecyclerView();

    }

    private void setupRecyclerView() {

        recyclerViewDetail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewDetail.setLayoutManager(layoutManager);
        mAdapter = new IngredientsAndStepsAdapter(getApplicationContext(), recipeObjects, mTwoPane, this);
        recyclerViewDetail.setAdapter(mAdapter);

    }

    @Override
    public void onStepClick(Step step) {
        if (step != null){
            if (mTwoPane){
                RecipeStepFragment fragment = new RecipeStepFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
            } else {
                Intent intent = new Intent(this, RecipeStepActivity.class);
                intent.putExtra(RecipeStepActivity.EXTRA, step);
                intent.putExtra(RecipeStepActivity.EXTRA_NAME, recipeName);
                intent.putParcelableArrayListExtra(RecipeStepActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) steps);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onPreviousClick(Step step) {

    }

    @Override
    public void onNextClick(Step step) {

    }
}
