package exercise4_events.refactored.listener;

import exercise4_events.refactored.event.*;
import exercise4_events.refactored.model.AuditLogEntry;

import java.util.ArrayList;
import java.util.List;

public class AuditLogListener implements EventListener {
    private final List<AuditLogEntry> logs;

    public AuditLogListener() {
        this.logs = new ArrayList<>();
    }

    @Override
    public void onEvent(Event event) {
        AuditLogEntry log = new AuditLogEntry(event.getTimestamp(), event.getEventType());
        logs.add(log);
        System.out.println("Event logged");
    }

    @Override
    public boolean supports(Class<? extends Event> event) {
        return (UserRegisteredEvent.class.isAssignableFrom(event) ||
                OrderPlacedEvent.class.isAssignableFrom(event) ||
                PaymentReceivedEvent.class.isAssignableFrom(event) ||
                SystemAlertEvent.class.isAssignableFrom(event));
    }

    public List<AuditLogEntry> getLogs() {
        return new ArrayList<>(logs);
    }
}
