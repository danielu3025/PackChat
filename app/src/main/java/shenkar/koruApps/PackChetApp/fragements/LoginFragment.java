package shenkar.koruApps.PackChetApp.fragements;


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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

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
        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userMail.getText().toString().isEmpty() && !userPass.getText().toString().isEmpty()){
                   model.mAuth.signInWithEmailAndPassword(userMail.getText().toString(), userPass.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("MAuth:", "signInWithEmail:success");
                                        FirebaseUser user = model.mAuth.getCurrentUser();
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
                if (!userMail.getText().toString().isEmpty() && !userPass.getText().toString().isEmpty()){
                   model.mAuth.createUserWithEmailAndPassword(userMail.getText().toString(), userPass.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("mAuth", "createUserWithEmail:success");
                                        FirebaseUser user = model.mAuth.getCurrentUser();
                                        EventBus.getDefault().post(new ReplaceMainFragmentEvent("courses"));

                                    } else {
                                        // If sign in fails, display a message to the user.
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
