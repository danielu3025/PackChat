package shenkar.koruApps.PackChetApp.objects;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    public ArrayAdapter<String> listAdapter;
    public String currantCourse ="";
    public boolean listFlag = false;
    public int selectedCourseNum= 0;
    public String currentGroup ="course-main";
    public int itemsOnCoursesListView =0;
    public String fcmToken = "";
    public ArrayList<String> toDoitems;
    public ArrayAdapter<String> toDoitemsAdapter;
    public ListView TodolvItems;
    public boolean safeMove  = false;
    public String userCode = "usercode";
    public  Utils utils = new Utils();
    public String wigetCourse ="";
    public String storageOption ="";
    public ValueEventListener stikyRefarnce ;
    public ArrayList<String> stikyList = new ArrayList<>();
    public String calenderOption ="";



}
