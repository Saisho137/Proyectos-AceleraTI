package exercise4_events.refactored.event;

import java.time.LocalDateTime;

public interface Event {
    LocalDateTime getTimestamp();

    String getEventType();
}
