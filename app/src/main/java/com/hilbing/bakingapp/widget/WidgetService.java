package com.hilbing.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.model.Ingredient;

import java.util.List;

public class WidgetService extends RemoteViewsService {

    private List<Ingredient> ingredients;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteListFactory(getApplicationContext());
    }

   private class RemoteListFactory implements RemoteViewsFactory{

        Context context;

        RemoteListFactory(Context applicationContext){
            this.context = applicationContext;
        }

       @Override
       public void onCreate() {

       }

       @Override
       public void onDataSetChanged() {
            ingredients = WidgetProvider.ingredients;
       }

       @Override
       public void onDestroy() {

       }

       @Override
       public int getCount() {
            if (ingredients == null) return 0;
           return ingredients.size();
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

       @Override
       public RemoteViews getViewAt(int position){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            Ingredient ingredient = ingredients.get(position);

            String measure = String.valueOf(ingredient.getQuantity());
            String widget_ingredient = ingredient.getIngredient();
            remoteViews.setTextViewText(R.id.widget_ing, widget_ingredient + " " + measure);

            return remoteViews;
       }
   }

}
