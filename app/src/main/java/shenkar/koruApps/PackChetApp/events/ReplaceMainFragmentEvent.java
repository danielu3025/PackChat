package shenkar.koruApps.PackChetApp.events;

/**
 * Created by danielluzgarten on 05/08/2017.
 */

public class ReplaceMainFragmentEvent {
    private final String message;

    public ReplaceMainFragmentEvent(String message) {
        this.message = message;
        System.out.println("Event Fired: " + "ReplaceMainFragmentEvent-"+message);
    }

    public String getMessage() {
        return message;
    }
}
