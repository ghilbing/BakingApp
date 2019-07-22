package com.hilbing.bakingapp;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.hilbing.bakingapp.fragment.RecipeFragment;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.recipes));

        Toast.makeText(this, R.string.recipes, Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

        RecipeFragment fragment = new RecipeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.recipe_fragment, fragment);
        transaction.commit();


    }

}
