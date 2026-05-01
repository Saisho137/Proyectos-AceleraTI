package exercise1_notifications.refactored;

import java.util.ArrayList;
import java.util.List;

public class NotificationLogger {
    private final List<NotificationLogEntry> notificationLog;

    public NotificationLogger() {
        this.notificationLog = new ArrayList<>();
    }

    public void log(NotificationLogEntry logEntry) {
        String log = String.format("[%s] %s - Type: %s, To: %s, Message: %s",
                logEntry.getDateTime().toString(), logEntry.isSuccess() ? "SUCCESS" : "FAILURE",
                logEntry.getNotificationType(), logEntry.getRecipient(), logEntry.getMessage());

        System.out.println(log);
        this.notificationLog.add(logEntry);
    }

    public List<NotificationLogEntry> getNotificationLog() {
        return new ArrayList<>(this.notificationLog);
    }
}
