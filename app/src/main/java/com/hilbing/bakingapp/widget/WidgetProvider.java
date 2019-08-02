package com.hilbing.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import com.google.gson.Gson;
import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.model.Recipe;


public class WidgetProvider extends AppWidgetProvider {

    public static void updateAllWidgets(final Context context, final Recipe recipe){
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String json = new Gson().toJson(recipe);
        sharedPreferences.edit().putString("recipe_on_widget", json).apply();
        final Class<WidgetProvider> widgetProviderClass = WidgetProvider.class;
        final Intent updateWidgetIntent = new Intent(context, WidgetProvider.class);
        updateWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        final int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, widgetProviderClass));
        updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(updateWidgetIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent){
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            final ComponentName componentName = new ComponentName(context, WidgetProvider.class);
            widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(componentName), R.id.lv_widget_ingredients);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for( int appWidgetId : appWidgetIds){
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            final String recipeJson = sharedPreferences.getString("recipe_on_widget", null);
            final Recipe recipe = (recipeJson == null) ? null : new Gson().fromJson(recipeJson, Recipe.class);
            if (recipe != null){
                remoteViews.setTextViewText(R.id.tv_widget_title, context.getString(R.string.widget_title, recipe.getName()));
                final Intent serviceIntent = new Intent(context, WidgetService.class);
                serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
                remoteViews.setRemoteAdapter(R.id.lv_widget_ingredients, serviceIntent);
            } else {
                remoteViews.setTextViewText(R.id.tv_widget_title, context.getString(R.string.widget_title_no_selection));
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
