package exercise4_events.refactored.listener;

import exercise4_events.refactored.event.*;

import java.util.HashMap;
import java.util.Map;

public class DashboardUpdateListener implements EventListener {
    private final Map<String, Integer> metricData;

    public DashboardUpdateListener() {
        this.metricData = new HashMap<>();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof UserRegisteredEvent) metricData.put("new_users",
                metricData.getOrDefault("new_users", 0) + 1);
        if (event instanceof OrderPlacedEvent) metricData.put("orders_today",
                metricData.getOrDefault("orders_today", 0) + 1);
    }

    @Override
    public boolean supports(Class<? extends Event> event) {
        return (UserRegisteredEvent.class.isAssignableFrom(event) ||
                OrderPlacedEvent.class.isAssignableFrom(event) ||
                PaymentReceivedEvent.class.isAssignableFrom(event) ||
                SystemAlertEvent.class.isAssignableFrom(event));
    }

    public int getMetric(String metric) {
        return metricData.getOrDefault(metric, 0);
    }
}
