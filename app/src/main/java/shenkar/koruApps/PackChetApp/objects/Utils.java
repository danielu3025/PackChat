package shenkar.koruApps.PackChetApp.objects;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class Utils {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public DatabaseReference getDbRef(String path){
        String[] stons = path.split("/",-1);
        DatabaseReference reference  = Model.getInstance().database.getReference();
        for (String ston : stons){
            reference = reference.child(ston);
        }
        return reference;
    }


    public void appendNode(String path, String nodeName , String nodeKey, String nodeValue){
        DatabaseReference reference = getDbRef(path);
        if (Objects.equals(nodeKey,"random")){
            reference.child(nodeName).push().setValue(nodeValue);
        }
        else {
            reference.child(nodeName).child(nodeKey).setValue(nodeValue);
        }
    }

    public void createUser(String userId, String username, String token, String avatar){
        appendNode("users",userId,"username",username);
        appendNode("users",userId,"token",token);
        appendNode("users",userId,"userImg",avatar);
        //set sub-trees
        appendNode("users/"+userId+"/Courses/Shenkar","calendar","0","0");
        appendNode("users/"+userId+"/Courses/Shenkar","stiky","0","0");
        appendNode("users/"+userId+"/Courses/Shenkar","toDoList","0","0");
    }
}
