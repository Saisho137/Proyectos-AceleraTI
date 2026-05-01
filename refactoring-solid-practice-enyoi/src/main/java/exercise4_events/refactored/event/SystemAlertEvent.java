package exercise4_events.refactored.event;

public class SystemAlertEvent extends BaseEvent {
    private final AlertLevel level;
    private final String content;
    private final String name;

    public SystemAlertEvent(AlertLevel level, String message, String name) {
        this.level = level;
        this.content = message;
        this.name = name;
    }

    @Override
    public String getEventType() {
        return "SYSTEM_ALERT";
    }

    public AlertLevel getLevel() {
        return level;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }
}
