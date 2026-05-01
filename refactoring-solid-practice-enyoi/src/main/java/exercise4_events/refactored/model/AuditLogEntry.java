package exercise4_events.refactored.model;

import java.time.LocalDateTime;

public class AuditLogEntry {
    public final LocalDateTime timestamp;
    public final String eventType;

    public AuditLogEntry(LocalDateTime timestamp, String eventType) {
        this.timestamp = timestamp;
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }
}
