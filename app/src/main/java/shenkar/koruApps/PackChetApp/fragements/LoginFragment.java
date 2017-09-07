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
import com.google.firebase.auth.AuthResult;
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
public class LoginFragment extends Fragment {

    Button loginbt;
    Button signUpBt;
    EditText userMail;
    EditText userPass;
    EditText userName;
    public static String uname;

    Model model = Model.getInstance();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginbt = (Button)view.findViewById(R.id.loginBt);
        signUpBt = (Button)view.findViewById(R.id.signUpBt);
        userMail = (EditText)view.findViewById(R.id.userMail);
        userPass = (EditText)view.findViewById(R.id.userPass);
        userName = (EditText)view.findViewById(R.id.userName);

        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = userName.getText().toString();
                if (!userMail.getText().toString().isEmpty() && !userPass.getText().toString().isEmpty() && !userName.getText().toString().isEmpty()){
                   model.mAuth.signInWithEmailAndPassword(userMail.getText().toString(), userPass.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("MAuth:", "signInWithEmail:success");
                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(uname)
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

                                        EventBus.getDefault().post(new ReplaceMainFragmentEvent("courses"));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d("MAuth:", "signInWithEmail:Field");
                                    }
                                }
                            });
                }else {
                    System.out.println("empty fields");
                }


            }
        });
        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = userName.getText().toString();
                System.out.println(userName.getText().toString());

                if (!userMail.getText().toString().isEmpty() && !userPass.getText().toString().isEmpty() && !userName.getText().toString().isEmpty()){
                   model.mAuth.createUserWithEmailAndPassword(userMail.getText().toString(), userPass.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("mAuth", "createUserWithEmail:success");

                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(uname)
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

                                        model.utils.createUser(user.getUid(),user.getDisplayName(),"123","321");


                                        EventBus.getDefault().post(new ReplaceMainFragmentEvent("courses"));


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        System.out.println("user is all ready sing to the app");
                                        Log.w("mAuth", "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });

                }else {
                    System.out.println("empty fields");
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = model.mAuth.getCurrentUser();
        if (currentUser != null){
            System.out.println("user is logged all ready");
            EventBus.getDefault().post(new ReplaceMainFragmentEvent("courses"));
        }
    }
}
