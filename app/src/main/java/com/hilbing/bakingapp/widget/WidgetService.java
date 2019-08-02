package com.hilbing.bakingapp.widget;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {

    public static final String RECIPE_DATA = "RECIPE_DATA";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(getApplicationContext());
    }


}

