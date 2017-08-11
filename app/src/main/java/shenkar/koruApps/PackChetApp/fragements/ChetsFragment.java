package shenkar.koruApps.PackChetApp.fragements;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.events.OpenConversationEvent;
import shenkar.koruApps.PackChetApp.events.ReplaceMainFragmentEvent;
import shenkar.koruApps.PackChetApp.objects.Model;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChetsFragment extends Fragment {
    Model model = Model.getInstance();

    public ChetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_chets, container, false);
        model.dbRef = model.database.getReference().child("Courses");
        model.groupsList = (ListView) view.findViewById(R.id.chatsList);
        model.groups = new ArrayList<>();

        model.dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                model.groups.removeAll(model.groups);
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    model.groups.add(item.getKey().toString());
                }
                updateList();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        model.groupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("conversations"));
                EventBus.getDefault().post(new OpenConversationEvent(model.groups.get(position)));

            }
        });

        return view;
    }
    private void updateList(){
        model.listAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, model.groups);
        model.groupsList.setAdapter(model.listAdapter);
    }


}
