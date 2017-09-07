package shenkar.koruApps.PackChetApp.fragements;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.objects.Model;
import shenkar.koruApps.PackChetApp.objects.Utils;

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
        return inflater.inflate(R.layout.fragment_bot, container, false);
    }

}
