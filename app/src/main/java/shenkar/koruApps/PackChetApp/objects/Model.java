package shenkar.koruApps.PackChetApp.objects;

import android.support.design.widget.FloatingActionButton;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public ArrayAdapter <String> listAdapter;
    public String currantChat="Course 1";
    public boolean listFlag = false;
    public int selectedCourseNum= 0;
    public String courseName = "All";
    public int itemsOnCoursesListView =0;
}
