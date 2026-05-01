package exercise4_events.refactored.publisher;

import exercise4_events.refactored.event.Event;
import exercise4_events.refactored.listener.EventListener;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private final List<EventListener> listeners;

    public EventPublisher() {
        this.listeners = new ArrayList<>();
    }

    public List<EventListener> getListeners() {
        return new ArrayList<>(listeners);
    }

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        Class<? extends EventListener> listenerClass = listener.getClass();
        listeners.removeIf(listenerClass::isInstance);
    }

    public int getListenerCount() {
        return listeners.size();
    }

    public void publish(Event event) {
        listeners.forEach(listener -> {
            if (listener.supports(event.getClass())) {
                listener.onEvent(event);
            }
        });
    }
}
