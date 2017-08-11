package shenkar.koruApps.PackChetApp.events;

public class OpenConversationEvent {
    private final String message;

    public OpenConversationEvent(String message) {
        this.message = message;
        System.out.println("Event Fired: " + "OpenConversationEvent-"+message);
    }
    public String getMessage() {
        return message;
    }
}
