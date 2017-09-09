package shenkar.koruApps.PackChetApp.fragements;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

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
    TextView lbStikey;



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
        lbStikey = (TextView)view.findViewById(R.id.lbstikey);
        model.stikyList.removeAll(model.stikyList);
        model.dbRef = model.dbRef.child(model.currantCourse).child("groups").child(model.currentGroup).child("messages");
        updateList();
        setSendlistner();
        return view;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        model.utils.getDbRef("db/" + model.currantCourse + "/stiky").removeEventListener(model.stikyRefarnce);
    }

    public void  setSendlistner(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().trim().equals("")) {

                } else {
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    DatabaseReference reference = model.dbRef;

                    reference.getRef().push().setValue(new ChatMessage(
                            input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            model.currantCourse)
                    );

                   model.utils.getDbRef("notifications").push().setValue(new ChatMessage(
                            input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            model.currantCourse)
                    );

                    input.setText("");
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String time = String.valueOf(adapter.getItem(position).getMessageTime());
                if (Objects.equals(adapter.getItem(position).getMessageUserId(), model.mAuth.getCurrentUser().getUid())){
                    model.utils.getDbRef("db/"+model.currantCourse+"/admins").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(model.mAuth.getCurrentUser().getUid())){
                                model.utils.appendNode("db/"+model.currantCourse,"stiky",time,adapter.getItem(position).getMessageText());
                                Toast toast = Toast.makeText(activity, "message is sticky now ", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 285);
                                toast.show();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}});
                }
                return true;
            }
        });

        model.stikyRefarnce = model.utils.getDbRef("db/"+model.currantCourse+"/stiky").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (Objects.equals(ds.getValue(),"0")){
                        model.stikyList.add("Sticky board is empty");
                    }
                    else {
                        model.stikyList.add(ds.getValue().toString());
                    }
                }
                if (model.stikyList.size()>0){
                    lbStikey.setText(model.stikyList.get(model.stikyList.size()-1));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
    public void updateList(){
        adapter = new MessageAdapter(getActivity(), ChatMessage.class, R.layout.item_in_message,model.dbRef);
        listView.setAdapter(adapter);
    }

}
