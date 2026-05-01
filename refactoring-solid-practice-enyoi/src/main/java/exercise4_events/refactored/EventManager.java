package exercise4_events.refactored;

import exercise4_events.refactored.event.Event;
import exercise4_events.refactored.listener.EventListener;
import exercise4_events.refactored.publisher.EventPublisher;

public class EventManager {
    private final EventPublisher eventPublisher;

    public EventManager(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void emit(Event event) {
        this.eventPublisher.publish(event);
    }

    public void registerListener(EventListener listener) {
        this.eventPublisher.subscribe(listener);
    }

    public void removeListener(EventListener listener) {
        this.eventPublisher.unsubscribe(listener);
    }
}
