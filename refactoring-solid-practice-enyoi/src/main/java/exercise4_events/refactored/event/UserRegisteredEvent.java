package exercise4_events.refactored.event;

public class UserRegisteredEvent extends BaseEvent {
    private final String email;
    private final String name;

    public UserRegisteredEvent(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @Override
    public String getEventType() {
        return "USER_REGISTERED";
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
