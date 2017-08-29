package shenkar.koruApps.PackChetApp.fragements;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.objects.ChatMessage;
import shenkar.koruApps.PackChetApp.objects.MessageAdapter;
import shenkar.koruApps.PackChetApp.objects.Model;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {
    Model model = Model.getInstance();
    Activity activity;

    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listView;
    private EditText input;
    private FloatingActionButton fab;


    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        listView = (ListView)view.findViewById(R.id.list);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        input = (EditText) view.findViewById(R.id.input);

        model.dbRef = model.dbRef.child(model.currantCourse).child("groups").child(model.currentGroup).child("messages");

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference dbRef = database.getReference("Courses");
//        dbRef = dbRef.child("Java");
//        model.dbRef = dbRef;

        adapter = new MessageAdapter(getActivity(), ChatMessage.class, R.layout.item_in_message,model.dbRef);
        listView.setAdapter(adapter);

        setSendlistner();

        return view;
    }
    public void  setSendlistner(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().trim().equals("")) {

                } else {
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    model.dbRef.getRef()
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                    FirebaseAuth.getInstance().getCurrentUser().getUid())
                            );
                    input.setText("");
                }
            }
        });
    }

}
