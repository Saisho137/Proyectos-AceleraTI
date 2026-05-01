package exercise4_events.refactored.listener;

import exercise4_events.refactored.event.Event;
import exercise4_events.refactored.event.OrderPlacedEvent;
import exercise4_events.refactored.event.SystemAlertEvent;

public class SmsNotificationListener implements EventListener {
    @Override
    public void onEvent(Event event) {
        System.out.println(formatMessageFor(event));
    }

    @Override
    public boolean supports(Class<? extends Event> event) {
        return (OrderPlacedEvent.class.isAssignableFrom(event) ||
                SystemAlertEvent.class.isAssignableFrom(event));
    }

    public String formatMessageFor(Event event) {
        if (event instanceof OrderPlacedEvent)
            return "SMS to " + ((OrderPlacedEvent) event).getNumber() + ": Your Order " + ((OrderPlacedEvent) event).getOrderId() + " has been placed";
        if (event instanceof SystemAlertEvent)
            return "CRITICAL Alert SMS to " + "admin_phone " + ((SystemAlertEvent) event).getName();
        return "";
    }
}
