package shenkar.koruApps.PackChetApp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import shenkar.koruApps.PackChetApp.AppWidgetConfigureActivity;
import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.objects.Utils;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetConfigureActivity AppWidgetConfigureActivity}
 */
public class AppWidget extends AppWidgetProvider {




    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        System.out.println("widget-updateAppWidget");

        CharSequence widgetText = AppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        System.out.println("widget-onUpdate");
        // There may be multiple widgets active, so update all of them
        for (final int appWidgetId : appWidgetIds) {
//          updateAppWidget(context, appWidgetManager, appWidgetId);
            final RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.app_widget);

            final String widgetText = AppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
            FirebaseDatabase.getInstance().getReference().child("db").child(widgetText).child("stiky").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String temp = "";
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        temp = ds.getValue().toString();
                    }
                    views.setTextViewText(R.id.appwidget_text,widgetText+": "+temp);
                    appWidgetManager.updateAppWidget(appWidgetId, views);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            AppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.app_widget);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }
}

