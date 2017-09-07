package shenkar.koruApps.PackChetApp.objects;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by danielluzgarten on 05/08/2017.
 */

public class Model {
    private static final Model ourInstance = new Model();

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public ListView groupsList;
    public ArrayList<String> groups;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference dbRef ;
    public DatabaseReference currentCoursedbRef;
    public StorageReference strogRef = FirebaseStorage.getInstance().getReference();
    public ArrayAdapter <String> listAdapter;
    public String currantCourse ="All";
    public boolean listFlag = false;
    public int selectedCourseNum= 0;
    public String currentGroup ="course-main";
    public int itemsOnCoursesListView =0;
    public String fcmToken = "";
    public ArrayList<String> toDoitems;
    public ArrayAdapter<String> toDoitemsAdapter;
    public ListView TodolvItems;
    public boolean safeMove  = false;
    public String  userCode = "usercode";
    public  Utils utils = new Utils();
    public  String wigetCourse ="";

}
