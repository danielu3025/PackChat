package shenkar.koruApps.PackChetApp.fragements;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Objects;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.events.CalnderEvent;
import shenkar.koruApps.PackChetApp.objects.Model;
import shenkar.koruApps.PackChetApp.objects.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalenderFragment extends Fragment {
    Utils utils = new Utils();
    Model model = Model.getInstance();
    Activity activity;

    CalendarView calnder ;
    TextView info;
    TextView lessonInfo;
    DatabaseReference dateRef = model.database.getReference().child("db");
    DatabaseReference assigmnetRef;
    String text = "Assignments:";
    String lessonTxt  = "Lectures:";
    Button btlesson,btAssi;
    String title ="";
    DatabaseReference reference;
    String key ="";
    ArrayList<String> list ;
    ArrayAdapter<String> adapter ;
    ListView listView;

    public CalenderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        // Inflate the layout for this fragment
        activity = getActivity();
        View view =  inflater.inflate(R.layout.fragment_clander, container, false);
        calnder = (CalendarView)view.findViewById(R.id.calendar);
        btAssi = (Button)view.findViewById(R.id.btAssi);
        btlesson = (Button)view.findViewById(R.id.btLect);
        listView = (ListView)view.findViewById(R.id.listCalnder);
        list = new ArrayList<>();
        adapter= new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1, list);
        buttsListenrs();
        buttonslogic();
        setCalnderOnClick();

        return view;
    }

    public void buttsListenrs(){
        btAssi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.calenderOption = "assignments";
                buttonslogic();
                title = "Assignments";
                EventBus.getDefault().post(new CalnderEvent(key,model.calenderOption));

            }
        });
        btlesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.calenderOption = "lecture";
                buttonslogic();
                title = "Lessons";
                EventBus.getDefault().post(new CalnderEvent(key,model.calenderOption));
            }
        });
    }

    public void buttonslogic(){
        if (Objects.equals(model.calenderOption , "lecture")){
            btlesson.setBackgroundColor(Color.rgb(89,138,196));
            btAssi.setBackgroundColor(Color.rgb(212,215,215));
        }
        else{
            btlesson.setBackgroundColor(Color.rgb(212,215,215));
            model.calenderOption = "assignments";
            btAssi.setBackgroundColor(Color.rgb(89,138,196));
        }
    }



    public void setCalnderOnClick(){
        calnder.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, final int month, final int dayOfMonth) {
                key = String.valueOf(dayOfMonth) + String.valueOf(month+1) + String.valueOf(year);
                System.out.println(dayOfMonth);
                System.out.println(month+1);
                System.out.println(year);
                text = title+"\n";
                EventBus.getDefault().post(new CalnderEvent(key,model.calenderOption));
            }
        });
    }
    @Subscribe
    public void EventReciver(CalnderEvent event){
        reference = utils.getDbRef("db/"+model.currantCourse+"/calendar/"+event.getKey()+"/"+event.getOption());
        text = "";
        list.removeAll(list);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp = "";
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    temp = ds.getKey() + ": ";
                    for (DataSnapshot cds: ds.getChildren()){
                        temp = temp + cds.getValue() + " ";
                    }
                    System.out.println(temp);
                    list.add(temp);
                    text = text + temp + "\n";
                }
                updateList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void updateList(){
        if (model.safeMove){
            adapter = new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
        }
    }
}
