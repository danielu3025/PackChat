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
import shenkar.koruApps.PackChetApp.fragements.BotFragment;
import shenkar.koruApps.PackChetApp.fragements.CalenderFragment;
import shenkar.koruApps.PackChetApp.fragements.CoursesFragment;
import shenkar.koruApps.PackChetApp.fragements.ConversationFragment;
import shenkar.koruApps.PackChetApp.fragements.MenuFragment;
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
    Button bcourses;
    Button bmenu;
    TextView title;
    FragmentManager manager;
    LoginFragment loginFragment;
    CoursesFragment coursesFragment;
    ConversationFragment conversationFragment;
    CalenderFragment calenderFragment;
    ToDoListFragment toDoListFragment;
    StorageFragment storageFragment;
    MenuFragment menuFragment;
    BotFragment botFragment;

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
        bcourses = (Button)findViewById(R.id.bcourses);
        bmenu = (Button)findViewById(R.id.bmenu);

        loginFragment = new LoginFragment();
        coursesFragment = new CoursesFragment();
        conversationFragment = new ConversationFragment();
        calenderFragment= new CalenderFragment();
        toDoListFragment = new ToDoListFragment();
        storageFragment = new StorageFragment();
        botFragment = new BotFragment();
        menuFragment =new MenuFragment();




        manager = getSupportFragmentManager();

        manager.beginTransaction()
                .replace(R.id.mainLayout,loginFragment,loginFragment.getTag())
                .commit();

        setListenrs();
    }
    public void setListenrs(){
        bcourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("courses"));
            }
        });

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
        brobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("bot"));
            }
        });
        bmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReplaceMainFragmentEvent("options"));
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
        initTabColor();
        switch (event.getMessage()){
            case "login":
                temp = loginFragment;
                title.setText("Login");
                break;
            case "bot":
                temp = botFragment;
                title.setText(model.currantCourse + " - Bot");
                brobot.setBackgroundResource(R.drawable.robot2 );

                break;
            case "chats":
                temp = conversationFragment;
                title.setText(model.currantCourse + " - chat");
                bchat.setBackgroundResource(R.drawable.chat2);

                break;
            case "calender":
                temp = calenderFragment;
                title.setText(model.currantCourse + " - Calender");
                bcalnder.setBackgroundResource(R.drawable.calendar2);

                break;
            case "toDoList":
                temp = toDoListFragment;
                title.setText(model.currantCourse + " - Todo-list");
                blist.setBackgroundResource(R.drawable.list2);

                break;
            case "storage":
                temp = storageFragment;
                title.setText(model.currantCourse + " - Files");
                bfolder.setBackgroundResource(R.drawable.folder2);

                break;
            case "courses":
                 temp = coursesFragment;
                 title.setText("Courses");
                 bcourses.setBackgroundResource(R.drawable.hut2);
                break;
            case "options":
                temp = menuFragment;
                title.setText("options");
                bmenu.setBackgroundResource(R.drawable.x);
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
        model.currantCourse = event.getMessage();
        title.setText(event.getMessage());
    }
    public void initTabColor(){
         bchat.setBackgroundResource(R.drawable.chat1);
         bcalnder.setBackgroundResource(R.drawable.calendar);
         blist.setBackgroundResource(R.drawable.list);
         bfolder.setBackgroundResource(R.drawable.folder);
         brobot.setBackgroundResource(R.drawable.robot);
         bcourses.setBackgroundResource(R.drawable.hut);
         bmenu.setBackgroundResource(R.drawable.menulines);
    }

}
