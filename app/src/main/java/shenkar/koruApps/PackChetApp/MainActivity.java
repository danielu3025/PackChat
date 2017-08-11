package shenkar.koruApps.PackChetApp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import shenkar.koruApps.PackChetApp.events.OpenConversationEvent;
import shenkar.koruApps.PackChetApp.events.ReplaceMainFragmentEvent;
import shenkar.koruApps.PackChetApp.fragements.ChetsFragment;
import shenkar.koruApps.PackChetApp.fragements.ConversationFragment;
import shenkar.koruApps.PackChetApp.fragements.LoginFragment;
import shenkar.koruApps.PackChetApp.objects.Model;

public class MainActivity extends AppCompatActivity {
    Model model = Model.getInstance();
    Button b1 ;
    Button b2 ;
    FragmentManager manager;
    LoginFragment loginFragment;
    ChetsFragment chetsFragment;
    ConversationFragment conversationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);


        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        b1 = (Button)findViewById(R.id.b1);
        b2 = (Button)findViewById(R.id.b2);


        loginFragment = new LoginFragment();
        chetsFragment = new ChetsFragment();
        conversationFragment = new ConversationFragment();



        manager = getSupportFragmentManager();

        manager.beginTransaction()
                .replace(R.id.mainLayout,loginFragment,loginFragment.getTag())
                .commit();

        setListenrs();
    }
    public void setListenrs(){
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction()
                        .replace(R.id.mainLayout,chetsFragment,chetsFragment.getTag())
                        .commit();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction()
                        .replace(R.id.mainLayout,loginFragment,loginFragment.getTag())
                        .commit();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void changeFragementtoRoom(ReplaceMainFragmentEvent event) {
        Fragment temp ;
        switch (event.getMessage()){
            case "login":
                temp = loginFragment;
                break;
            case "conversations":
                temp = conversationFragment;
                break;
            case "chats":
                temp = chetsFragment;
                break;
            default:
                temp = loginFragment;
        }
        manager.beginTransaction()
                .replace(R.id.mainLayout,temp,temp.getTag())
                .commit();
    }

    @Subscribe
    public void openConversiotn(OpenConversationEvent event) {
        model.currantChat = event.getMessage();
    }
}
