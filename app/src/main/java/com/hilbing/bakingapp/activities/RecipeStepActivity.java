package com.hilbing.bakingapp.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.fragment.RecipeStepFragment;
import com.hilbing.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnStepClickListener {

    public static final String EXTRA = "Step";
    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_LIST = "Step List";
    private static final String STEP_LIST = "Current";
    private static final String STEP_INDEX = "Index";

    private Step step;
    private List<Step> stepList;
    private int stepIdx;
    private String recipeName;

    @BindView(R.id.step_toolbar)
    Toolbar stepToolbar;

    @BindView(R.id.step_detail_container)
    FrameLayout stepContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ButterKnife.bind(this);

        setSupportActionBar(stepToolbar);

        Intent getIntent = getIntent();

        if (getIntent == null){
            finish();
        }

        recipeName = getIntent.getStringExtra(EXTRA_NAME);
        setTitle(recipeName);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            step = getIntent().getParcelableExtra((EXTRA));
            stepList = getIntent().getParcelableArrayListExtra(RecipeStepActivity.EXTRA_LIST);
            addFragment();
        }
        else {
            stepList = savedInstanceState.getParcelableArrayList(STEP_LIST);
            stepIdx = savedInstanceState.getInt(STEP_INDEX);

        }
    }

    private void addFragment() {
        RecipeStepFragment fragment = RecipeStepFragment.newInstance(step);
        getSupportFragmentManager().beginTransaction().add(R.id.step_detail_container, fragment).commit();

    }


    @Override
    public void onPreviousClick(Step step) {
        stepIdx = step.getId();
        if (stepIdx > 0){
            showStep(stepList.get(stepIdx-1));
        } else {
            finish();
        }
    }

    @Override
    public void onNextClick(Step step) {
        stepIdx = step.getId();
        if (stepIdx < stepList.size()-1){
            showStep(stepList.get(stepIdx+1));
        } else {
            finish();
        }
    }

    private void showStep(Step step){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        RecipeStepFragment fragment = RecipeStepFragment.newInstance(step);
        transaction.replace(R.id.item_detail_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
