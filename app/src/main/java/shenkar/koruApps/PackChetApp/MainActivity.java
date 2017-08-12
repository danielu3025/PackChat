package shenkar.koruApps.PackChetApp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import shenkar.koruApps.PackChetApp.events.OpenConversationEvent;
import shenkar.koruApps.PackChetApp.events.ReplaceMainFragmentEvent;
import shenkar.koruApps.PackChetApp.fragements.CalenderFragment;
import shenkar.koruApps.PackChetApp.fragements.ChetsFragment;
import shenkar.koruApps.PackChetApp.fragements.ConversationFragment;
import shenkar.koruApps.PackChetApp.fragements.StorageFragment;
import shenkar.koruApps.PackChetApp.fragements.ToDoListFragment;
import shenkar.koruApps.PackChetApp.fragements.LoginFragment;
import shenkar.koruApps.PackChetApp.objects.Model;

public class MainActivity extends AppCompatActivity {
    Model model = Model.getInstance();
    Button bchat ;
    Button bcalnder ;
    Button blist ;
    Button bfolder ;
    Button brobot ;
    TextView title;
    FragmentManager manager;
    LoginFragment loginFragment;
    ChetsFragment chetsFragment;
    ConversationFragment conversationFragment;
    CalenderFragment calenderFragment;
    ToDoListFragment toDoListFragment;
    StorageFragment storageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);


        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        title = (TextView)findViewById(R.id.titleText) ;
        bchat = (Button)findViewById(R.id.bchats);
        bcalnder = (Button)findViewById(R.id.bclander);
        blist = (Button)findViewById(R.id.blist);
        bfolder = (Button)findViewById(R.id.bfolder);
        brobot = (Button)findViewById(R.id.brobot);



        loginFragment = new LoginFragment();
        chetsFragment = new ChetsFragment();
        conversationFragment = new ConversationFragment();
        calenderFragment= new CalenderFragment();
        toDoListFragment = new ToDoListFragment();
        storageFragment = new StorageFragment();




        manager = getSupportFragmentManager();

        manager.beginTransaction()
                .replace(R.id.mainLayout,loginFragment,loginFragment.getTag())
                .commit();

        setListenrs();
    }
    public void setListenrs(){
        bchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("chats"));
            }
        });
        bcalnder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("calender"));
            }
        });
        blist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("toDoList"));
            }
        });
        bfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("storage"));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void changeFragement(ReplaceMainFragmentEvent event) {
        Fragment temp ;
        switch (event.getMessage()){
            case "login":
                temp = loginFragment;
                title.setText("Login");
                break;
            case "conversations":
                temp = conversationFragment;
                break;
            case "chats":
                temp = chetsFragment;
                title.setText("Courses");
                break;
            case "calender":
                temp = calenderFragment;
                title.setText("Calender");
                break;
            case "toDoList":
                temp = toDoListFragment;
                title.setText("Todo-list");
                break;
            case "storage":
                temp = storageFragment;
                title.setText("Files");
                break;
            default:
                temp = loginFragment;
                title.setText("Login");
        }
        manager.beginTransaction()
                .replace(R.id.mainLayout,temp,temp.getTag())
                .commit();
    }

    @Subscribe
    public void openConversiotn(OpenConversationEvent event) {
        model.currantChat = event.getMessage();
        title.setText(event.getMessage());

    }

}
