package shenkar.koruApps.PackChetApp.objects;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by danielluzgarten on 18/08/2017.
 */

public class SherdPrefHendler {

    private static Context hCtx;
    private static SherdPrefHendler hInstace;

    private SherdPrefHendler(Context context) {
        hCtx = context;
    }
    public static synchronized SherdPrefHendler gethInstace(Context context){
        if (hInstace == null){
            hInstace = new SherdPrefHendler(context);
        }
        return hInstace;
    }

    public boolean storeToken(String token){
        SharedPreferences sharedPreferences = hCtx.getSharedPreferences("fcmToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",token);
        editor.apply();
        return true;
    }
    public String getToken(){
        SharedPreferences sharedPreferences = hCtx.getSharedPreferences("fcmToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token",null);
    }
}
