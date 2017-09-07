package shenkar.koruApps.PackChetApp.fragements;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import shenkar.koruApps.PackChetApp.R;
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
    TextView assigmnetInfo;
    TextView lessonInfo;
    DatabaseReference dateRef = model.database.getReference().child("db");
    DatabaseReference assigmnetRef;
    String assigmentsTxt  = "Assignments:";
    String lessonTxt  = "Lectures:";



    public CalenderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        View view =  inflater.inflate(R.layout.fragment_clander, container, false);
        calnder = (CalendarView)view.findViewById(R.id.calendar);
        assigmnetInfo= (TextView)view.findViewById(R.id.assgimentsInfo);
        lessonInfo = (TextView)view.findViewById(R.id.lectInfo);

        assigmentsTxt  = "Assignments:\n";
        lessonTxt  = "Lectures:\n";

        setCalnderOnClick();

        return view;
    }

    public void setCalnderOnClick(){
        calnder.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, final int month, final int dayOfMonth) {
                final String key = String.valueOf(dayOfMonth) + String.valueOf(month+1) +String.valueOf(year);
                System.out.println(dayOfMonth);
                System.out.println(month+1);
                System.out.println(year);
                assigmentsTxt  = "Assignments:\n";
                lessonTxt  = "Lectures:\n";

                DatabaseReference reference = utils.getDbRef("users/"+model.userCode+"/Courses/"+model.currantCourse+"/calendar/"+key+"/assignments");
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
                            assigmentsTxt = assigmentsTxt + temp + "\n";
                        }
                        assigmnetInfo.setText(assigmentsTxt);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                utils.getDbRef("users/"+model.userCode+"/Courses/"+model.currantCourse+"/calendar/"+key+"/lecture").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String temp = "";
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            temp = ds.getKey() + ": ";
                            for (DataSnapshot cds: ds.getChildren()){
                                temp = temp + cds.getValue() + " ";
                            }
                            System.out.println(temp);
                            lessonTxt = lessonTxt + temp + "\n";
                        }
                        lessonInfo.setText(lessonTxt);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


}
