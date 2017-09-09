package shenkar.koruApps.PackChetApp.fragements;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.greenrobot.eventbus.EventBus;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.events.ReplaceMainFragmentEvent;
import shenkar.koruApps.PackChetApp.objects.Model;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    Button btsignOut ;
    Button btChange;
    EditText uName;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        btsignOut = (Button) view.findViewById(R.id.btOut);
        btChange = (Button)view.findViewById(R.id.btChange);
        uName = (EditText)view.findViewById(R.id.chagneName);
        btsignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("login"));
            }
        });
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!uName.getText().toString().isEmpty()){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("MAuth:", "signInWithEmail:success");
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(uName.getText().toString())
                            .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/packchet-6ce21.appspot.com/o/avatars%2Favatar.png?alt=media&token=9add8de4-55d6-4889-b295-f63b938f81d3"))
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("update", "User profile updated.  to " + user.getDisplayName());
                                    }
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("update", "User profile updated.  to " + user.getDisplayName());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("not updated", "User profile updated.  to " + user.getDisplayName());
                        }
                    });
                }
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("courses"));
            }
        });

        uName.setText(Model.getInstance().mAuth.getCurrentUser().getDisplayName());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        uName.setText(Model.getInstance().mAuth.getCurrentUser().getDisplayName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
