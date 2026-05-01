package exercise4_events.refactored.event;

import java.time.LocalDateTime;

public abstract class BaseEvent implements Event {
    @Override
    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }
}
