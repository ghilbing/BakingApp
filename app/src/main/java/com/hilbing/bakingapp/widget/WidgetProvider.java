package com.hilbing.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hilbing.bakingapp.MainActivity;
import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.activities.RecipeDetailActivity;
import com.hilbing.bakingapp.activities.RecipeStepActivity;
import com.hilbing.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class WidgetProvider extends AppWidgetProvider {

    static List<Ingredient> ingredients = new ArrayList<>();
    private static String widgetText;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){

        SharedPreferences sharedPreferences = context.getSharedPreferences(RecipeDetailActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(RecipeDetailActivity.ID_PREF, 0);
        widgetText = sharedPreferences.getString(RecipeDetailActivity.NAME_PREF, "no recipe");

        //Remote views
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        remoteViews.setTextViewText(R.id.widget_text, widgetText);

        //Set adapter
        Intent intent = new Intent(context, WidgetService.class);
        remoteViews.setRemoteAdapter(R.id.widget_list, intent);

        //open MainActivity when title is clicked
        Intent clickIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_text, pendingIntent);

        //Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);

    }

   public static void updateWidget(Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        //Update
       for (int appWidgetId : appWidgetIds){
           WidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);
           Toast.makeText(context, "Widget has been updated", Toast.LENGTH_LONG).show();
       }
   }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Could be multiple active widgets
        for (int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences sharedPreferences = context.getSharedPreferences(RecipeDetailActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(RecipeDetailActivity.NAME_PREF);
        editor.remove(RecipeDetailActivity.ID_PREF);
        editor.apply();
    }
}
