package com.hilbing.bakingapp.widget;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.model.Ingredient;
import com.hilbing.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.internal.Utils;


public class WidgetService extends RemoteViewsService {

    public static final String RECIPE_DATA = "RECIPE_DATA";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(getApplicationContext());
    }

    //    @Override
//    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        return new RemoteListFactory(getApplicationContext());
//    }
//
//    private class RemoteListFactory implements WidgetService.RemoteViewsFactory{
//
//        private Context mContext;
//        private List<Ingredient> ingredients;
//
//        public RemoteListFactory(Context applicationContext) {
//            this.mContext = applicationContext;
//        }
//
//        @Override
//        public void onCreate() {
//
//        }
//
//        @Override
//        public void onDataSetChanged() {
//            //Get the update ingredient string from shared preferences
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//            String ingredientsString = sharedPreferences.getString(getString(R.string.pref_ingredient_list_key), "");
//            //Convert ingredient string to a list
//            ingredients = Utils.toIngredientList(ingredientsString);
//           // ingredients = WidgetProvider.ingredients;
//        }
//
//        @Override
//        public void onDestroy() {
//
//        }
//
//        @Override
//        public int getCount() {
//            if (ingredients == null) return 0;
//            return ingredients.size();
//        }
//
//        @Override
//        public RemoteViews getViewAt(int position) {
//            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);
//            Ingredient ingredient = ingredients.get(position);
//
//            String measure = String.valueOf(ingredient.getQuantity());
//            String widget_ingredients = ingredient.getIngredient();
//            views.setTextViewText(R.id.widget_ing, widget_ingredients  + "   " + measure);
//
//            return views;
//        }
//
//        @Override
//        public RemoteViews getLoadingView() {
//            return null;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            return 1;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//    }

}

