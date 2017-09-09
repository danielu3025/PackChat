package shenkar.koruApps.PackChetApp.events;

/**
 * Created by danielluzgarten on 08/09/2017.
 */

public class CalnderEvent {
    String key;
    String option;

    public String getKey() {
        return key;
    }

    public String getOption() {
        return option;
    }

    public CalnderEvent(String key, String option) {
        this.key = key;
        this.option = option;
        System.out.println("Event Fired: " + "CalanderEvent-"+ key +" "+ option);
    }
}
