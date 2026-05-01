package exercise4_events.refactored.listener;

import exercise4_events.refactored.event.Event;

public interface EventListener {
    void onEvent(Event event);
    boolean supports(Class<? extends Event> eventClass);
}
