package com.hilbing.bakingapp.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.gson.Gson;
import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.model.Ingredient;
import com.hilbing.bakingapp.model.Recipe;
import java.util.ArrayList;
import java.util.List;


public class IngredientsRemoteViewsFactory implements WidgetService.RemoteViewsFactory, SharedPreferences.OnSharedPreferenceChangeListener {

    private static String TAG = IngredientsRemoteViewsFactory.class.getSimpleName();

    private Context mContext;
    private List<Ingredient> ingredients = new ArrayList<>();

    public IngredientsRemoteViewsFactory(Context context){
        mContext = context;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        fetchingIngredientsList(sharedPreferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("recipe_on_widget")) {
            fetchingIngredientsList(sharedPreferences);
        }
    }

    @Override
    public void onCreate() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public int getCount() {
        return (null == ingredients) ? 0 : ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient);
        final Ingredient ingredient = ingredients.get(position);
        final String ingredientText = mContext.getString(R.string.ingredient_format, String.valueOf(ingredient.getQuantity()), ingredient.getMeasure(), ingredient.getIngredient());
        remoteViews.setTextViewText(R.id.tv_ingredient, ingredientText);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void fetchingIngredientsList(SharedPreferences sharedPreferences) {
        final String recipeJson = sharedPreferences.getString("recipe_on_widget", null);
        Log.d("WIDGET", recipeJson);
        final Recipe recipe = (null == recipeJson) ? null : new Gson().fromJson(recipeJson, Recipe.class);
        Log.d(TAG, recipe.toString());
        if (recipe != null){
            ingredients = recipe.getIngredients();
            Log.d(TAG, ingredients.toArray().toString());
            onDataSetChanged();
        }

    }

}
