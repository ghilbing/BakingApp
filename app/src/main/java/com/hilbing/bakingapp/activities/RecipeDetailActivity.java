package com.hilbing.bakingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.adapter.IngredientsAndStepsAdapter;
import com.hilbing.bakingapp.fragment.RecipeStepFragment;
import com.hilbing.bakingapp.model.Ingredient;
import com.hilbing.bakingapp.model.Recipe;
import com.hilbing.bakingapp.model.Step;
import com.hilbing.bakingapp.widget.WidgetProvider;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements IngredientsAndStepsAdapter.StepClickListener, RecipeStepFragment.OnStepClickListener {

    private static String TAG = RecipeDetailActivity.class.getSimpleName();

    @Nullable
    @BindView(R.id.rv_detail)
    RecyclerView recyclerViewDetail;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.step_detail_container)
    FrameLayout detailContainer;

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

    public static final String WIDGET_PREF = "recipe_on_widget";
    public static final String ID_PREF = "id";
    public static final String NAME_PREF = "name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        recipeObjects = new ArrayList<Object>();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


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

        if (findViewById(R.id.step_detail_container) != null){
            //For larger screens
            mTwoPane = true;
        }

        if (detailContainer != null){
            mTwoPane = true;
        }

        setupRecyclerView();

    }

    private void setupRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewDetail.setLayoutManager(layoutManager);
        recyclerViewDetail.setHasFixedSize(true);
        mAdapter = new IngredientsAndStepsAdapter(getApplicationContext(), recipeObjects, mTwoPane, this);
        recyclerViewDetail.setAdapter(mAdapter);

    }

    @Override
    public void onStepClick(Step step) {
        if (step != null){
            if (mTwoPane){
                RecipeStepFragment fragment = RecipeStepFragment.newInstance(step);
                getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container, fragment).commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_widget, menu);
        return true;
    }

    @Override
    public void onPreviousClick(Step step) {
    }

    @Override
    public void onNextClick(Step step) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.menu.add_widget:
                return false;
               // addToPrefsWidget();
               // break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void addToPrefsWidget() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ID_PREF, recipeId);
        editor.putString(NAME_PREF, recipeName);
        editor.apply();

        //Add to widget
        WidgetProvider.updateAllWidgets(this, recipe);

    }

}
