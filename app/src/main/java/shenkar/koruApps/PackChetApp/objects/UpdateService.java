package shenkar.koruApps.PackChetApp.objects;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.Widget;
import shenkar.koruApps.PackChetApp.events.WidgetEvent;


public class UpdateService extends Service {
    public static Boolean update = false;
    Model model = Model.getInstance();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Utils utils = new Utils();
    String temp = "";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget);
        if (update) {
            view.setTextViewText(R.id.appwidget_text, temp);
            ComponentName theWidget = new ComponentName(this, Widget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(theWidget, view);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

    }
    @Subscribe
    public void setDbLIstenr(WidgetEvent event){
        utils.getDbRef("db/"+event.getMsg()+"/stiky").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("service update");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    temp = ds.getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
