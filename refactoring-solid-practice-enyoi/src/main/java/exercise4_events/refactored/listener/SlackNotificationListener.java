package exercise4_events.refactored.listener;

import exercise4_events.refactored.event.*;

public class SlackNotificationListener implements EventListener {
    @Override
    public void onEvent(Event event) {
        String channel = getChannelFor(event);
        String message = formatMessageFor(event);
        System.out.println("Slack [" + channel + "]: " + message);
    }

    @Override
    public boolean supports(Class<? extends Event> event) {
        return (UserRegisteredEvent.class.isAssignableFrom(event) ||
                OrderPlacedEvent.class.isAssignableFrom(event) ||
                PaymentReceivedEvent.class.isAssignableFrom(event) ||
                SystemAlertEvent.class.isAssignableFrom(event));
    }

    public String getChannelFor(Event event) {
        if (event instanceof UserRegisteredEvent) {
            return "#new-users";
        } else if (event instanceof OrderPlacedEvent) {
            return "#orders";
        } else if (event instanceof SystemAlertEvent) {
            return ((SystemAlertEvent) event).getLevel() == AlertLevel.CRITICAL ? "#alerts-critical" : "#alerts";
        }
        return "#general";
    }

    private String formatMessageFor(Event event) {
        if (event instanceof UserRegisteredEvent) {
            return "Nuevo Usuario Registrado: " + ((UserRegisteredEvent) event).getEmail();
        } else if (event instanceof OrderPlacedEvent) {
            return "Nueva Orden recibida: " + ((OrderPlacedEvent) event).getOrderId();
        } else if (event instanceof SystemAlertEvent) {
            return "ALERTA [" + ((SystemAlertEvent) event).getLevel() + "]: " + ((SystemAlertEvent) event).getContent();
        }
        return "Evento Recibido: " + event.getEventType();
    }
}
