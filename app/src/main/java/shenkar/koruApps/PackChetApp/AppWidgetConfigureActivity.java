package shenkar.koruApps.PackChetApp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import shenkar.koruApps.PackChetApp.events.WidgetEvent;
import shenkar.koruApps.PackChetApp.objects.Model;

/**
 * The configuration screen for the {@link AppWidget AppWidget} AppWidget.
 */
public class AppWidgetConfigureActivity extends Activity {
    Model model = Model.getInstance();
    DatabaseReference reference;
    ListView listWidget;
    Button btadd ;


    private static final String PREFS_NAME = "shenkar.koruApps.PackChetApp.AppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = AppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            AppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            model.wigetCourse = mAppWidgetText.getText().toString();
            //EventBus.getDefault().post(new WidgetEvent(mAppWidgetText.getText().toString()));

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public AppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);


        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        reference = model.utils.getDbRef("users/"+model.userCode+"/Courses");


        setContentView(R.layout.app_widget_configure);
        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        listWidget = (ListView)findViewById(R.id.listWidget);
        btadd = (Button)findViewById(R.id.add_button);

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = AppWidgetConfigureActivity.this;

                // When the button is clicked, store the string locally
                String widgetText = mAppWidgetText.getText().toString();
                saveTitlePref(context, mAppWidgetId, widgetText);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                AppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                model.wigetCourse = mAppWidgetText.getText().toString();
                //EventBus.getDefault().post(new WidgetEvent(mAppWidgetText.getText().toString()));


                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                model.groups.removeAll(model.groups);
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    model.groups.add(item.getKey().toString());
                }
                update();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listWidget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAppWidgetText.setText(model.listAdapter.getItem(position));
            }
        });




        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(loadTitlePref(AppWidgetConfigureActivity.this, mAppWidgetId));
    }

    private void update(){
        if (model.safeMove){
            model.listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, model.groups);
            listWidget.setAdapter(model.listAdapter);
        }
    }

}

