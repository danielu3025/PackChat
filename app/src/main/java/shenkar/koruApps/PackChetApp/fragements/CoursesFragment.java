package shenkar.koruApps.PackChetApp.fragements;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.events.OpenConversationEvent;
import shenkar.koruApps.PackChetApp.objects.Model;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoursesFragment extends Fragment {
    Model model = Model.getInstance();

    public CoursesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_chets, container, false);
        model.dbRef = model.database.getReference().child("db");
        model.groupsList = (ListView) view.findViewById(R.id.chatsList);
        model.groups = new ArrayList<>();

        model.dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                model.groups.removeAll(model.groups);
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    model.groups.add(item.getKey().toString());
                }
                addItemListenr();
                updateList();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        model.groupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //EventBus.getDefault().post(new ReplaceMainFragmentEvent("conversations"));
                EventBus.getDefault().post(new OpenConversationEvent(model.groups.get(position)));
                for (int i =0 ; i<parent.getChildCount();i++){
                    parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                model.currantCourse = model.groups.get(position);
                model.selectedCourseNum = position;
                view.setBackgroundColor(Color.YELLOW);

            }
        });

        return view;
    }
    private void updateList(){
        model.listAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, model.groups);
        model.groupsList.setAdapter(model.listAdapter);
        System.out.println(model.listAdapter.getCount());
    }

    public void addItemListenr(){

        final int[] oldFirstVisibleItem = {0};
        final int[] oldLastVisibleItem = {0};

        model.groupsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > oldFirstVisibleItem[0]) {
                    for(int i = oldFirstVisibleItem[0]; i < firstVisibleItem; i++) {
                        onExit(i);
                    }
                }
                if (firstVisibleItem < oldFirstVisibleItem[0]) {
                    for(int i = firstVisibleItem; i < oldFirstVisibleItem[0]; i++) {
                        onEnter(i);
                    }
                }

                int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
                if (lastVisibleItem < oldLastVisibleItem[0]) {
                    for(int i = oldLastVisibleItem[0] +1; i <= lastVisibleItem; i++) {
                        onExit(i);
                    }
                }
                if (lastVisibleItem > oldLastVisibleItem[0]) {
                    for(int i = oldLastVisibleItem[0] +1; i <= lastVisibleItem; i++) {
                        onEnter(i);
                    }
                }

                oldFirstVisibleItem[0] = firstVisibleItem;
                oldLastVisibleItem[0] = lastVisibleItem;
            }
        });
    }

    private void onExit(int i) {
    }
    private void onEnter(int i) {
        System.out.println("item Enter");
        model.itemsOnCoursesListView++;
        if (model.itemsOnCoursesListView == model.listAdapter.getCount()){
            model.groupsList.getChildAt(model.selectedCourseNum).setBackgroundColor(Color.YELLOW);
            model.itemsOnCoursesListView = 0;
            model.currantCourse = model.groups.get(model.selectedCourseNum);
        }

    }

}
