package exercise4_events.refactored.listener;

import exercise4_events.refactored.event.Event;
import exercise4_events.refactored.event.OrderPlacedEvent;
import exercise4_events.refactored.event.PaymentReceivedEvent;
import exercise4_events.refactored.event.UserRegisteredEvent;

public class EmailNotificationListener implements EventListener {
    @Override
    public void onEvent(Event event) {
        if (event instanceof UserRegisteredEvent) {
            System.out.println(" Sending welcome email to " + ((UserRegisteredEvent) event).getEmail());
            System.out.println("   Subject: Welcome to our platform, " + ((UserRegisteredEvent) event).getName() + "!");
        } else if (event instanceof OrderPlacedEvent) {
            System.out.println(" Sending order confirmation to " + ((OrderPlacedEvent) event).getEmail());
            System.out.println("   Order: " + ((OrderPlacedEvent) event).getOrderId());
        } else if (event instanceof PaymentReceivedEvent) {
            System.out.println(" Sending payment receipt to " + ((PaymentReceivedEvent) event).getEmail());
            System.out.println("   Payment: " + ((PaymentReceivedEvent) event).getPaymentId() + " - $" + ((PaymentReceivedEvent) event).getAmount());
        } else {
            throw new UnsupportedOperationException("Unsupported event type: " + event.getClass().getName());
        }
    }

    @Override
    public boolean supports(Class<? extends Event> eventClass) {
        return (
                eventClass.isAssignableFrom(UserRegisteredEvent.class) ||
                        eventClass.isAssignableFrom(OrderPlacedEvent.class) ||
                        eventClass.isAssignableFrom(PaymentReceivedEvent.class)
        );
    }
}
