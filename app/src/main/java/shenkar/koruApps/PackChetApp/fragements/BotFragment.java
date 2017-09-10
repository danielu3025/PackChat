package shenkar.koruApps.PackChetApp.fragements;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.objects.Model;

/**
 * A simple {@link Fragment} subclass.
 */
public class BotFragment extends Fragment {

    Model model = Model.getInstance();

    public BotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //model.utils.createUser("pN0OvnEUZPYkvoMa4nMvfvp0DGD3","daniel2","123","2312");

        return inflater.inflate(R.layout.fragment_bot, container, false);
    }

}
