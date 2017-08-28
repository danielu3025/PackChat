package shenkar.koruApps.PackChetApp.objects;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by danielluzgarten on 21/08/2017.
 */

public class Utils {

    public DatabaseReference getDbRef(String path){
        String[] stons = path.split("/",-1);
        DatabaseReference reference  = Model.getInstance().database.getReference();
        for (String ston : stons){
            reference = reference.child(ston);
        }
        return reference;
    }
}
