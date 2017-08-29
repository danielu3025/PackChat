package shenkar.koruApps.PackChetApp.events;

/**
 * Created by danielluzgarten on 28/08/2017.
 */

public class ChangeTypeOfStorageEvent {
    String msg;
    public ChangeTypeOfStorageEvent(String msg) {
        this.msg = msg;
        System.out.println("ChangeTypeOfStorageEvent fierd with: " + msg);
    }
    public String getMsg() {
        return msg;
    }
}
