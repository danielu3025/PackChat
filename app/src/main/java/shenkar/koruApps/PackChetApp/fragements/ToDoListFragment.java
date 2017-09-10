package shenkar.koruApps.PackChetApp.fragements;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.objects.Model;
import shenkar.koruApps.PackChetApp.objects.Utils;


public class ToDoListFragment extends Fragment {
    Model model = Model.getInstance();
    Activity activity;

    Utils utils = new Utils();
    ListView todoContainer;
    Button btAdd ;
    EditText etTodo;
    DatabaseReference dref;
    private ArrayList<String> items;

    private ArrayAdapter<String> itemsAdapter;

    class toDoListTask{
        public String key;
        public String valu;

        public toDoListTask(String key, String valu) {
            this.key = key;
            this.valu = valu;
        }
    }

    ArrayList<toDoListTask> toDoListTasks = new ArrayList<>();

    public ToDoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        todoContainer =(ListView) view.findViewById(R.id.toDoContainer);
        btAdd =(Button) view.findViewById(R.id.btNewTodo);
        etTodo =(EditText) view.findViewById(R.id.etTodo);
        items = new ArrayList<String>();



        dref = utils.getDbRef("users/"+model.userCode+"/Courses/"+model.currantCourse+"/toDoList");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        items.add((String) ds.getValue());
                        toDoListTasks.add(new toDoListTask(ds.getKey(),ds.getValue().toString()));
                    }

                    for (String s : items){
                        if (Objects.equals(s, "0")){
                            items.remove(s);
                            break;
                        }
                    }
                    for (toDoListTask t : toDoListTasks){
                        if (Objects.equals(t.key, "0")){
                            items.remove(t);
                            break;
                        }
                    }
                    updateList();
                    setListners();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        return view;
    }
    public void setListners(){
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTodo.getText().toString().isEmpty()){
                    DatabaseReference tempREF = null;
                    final String itemText = etTodo.getText().toString();
                    tempREF =dref.push();

                    final DatabaseReference finalTempREF = tempREF;
                    tempREF.setValue(itemText).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            toDoListTasks.add(new toDoListTask(finalTempREF.getKey(),itemText));
                            //toDoListTask tempTask = new toDoListTask(tempREF.getkey())
                            itemsAdapter.add(itemText);
                            etTodo.setText("");
                        }
                    });
                }
                model.utils.hideSoftKeyboard(activity);
            }
        });
        todoContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String toremove = items.get(position);
                String tempkey ="";
                int index =  0;
                for ( int i =0 ; i< toDoListTasks.size();i++ ){
                    if (toDoListTasks.get(i).valu == toremove ){
                        tempkey = toDoListTasks.get(i).key;
                        index = i;
                        break;
                    }
                }

                if (dref.child(tempkey).getKey() != null){
                    final int finalIndex = index;
                    dref.child(tempkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            toDoListTasks.remove(finalIndex);
                            items.remove(position);
                            itemsAdapter.notifyDataSetChanged();
                        }
                    });
                }
                return true;
            }
        });

        todoContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                model.utils.hideSoftKeyboard(activity);
            }
        });
    }
    private void updateList(){
        itemsAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_dropdown_item_1line,items);
        todoContainer.setAdapter(itemsAdapter);
    }
}
