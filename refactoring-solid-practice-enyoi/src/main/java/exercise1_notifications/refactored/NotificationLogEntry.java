package exercise1_notifications.refactored;

import java.time.LocalDateTime;

public class NotificationLogEntry {
    private LocalDateTime dateTime;
    private boolean success;
    private final String notificationType;
    private final String recipient;
    private final String message;

    public NotificationLogEntry(boolean success,
                                String notificationType,
                                String recipient,
                                String message) {
        this.dateTime = LocalDateTime.now();
        this.success = success;
        this.notificationType = notificationType;
        this.recipient = recipient;
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String toString(){
        return String.format("[%s] %s - Type: %s, To: %s, Message: %s",
                dateTime.toString(), success ? "SUCCESS" : "FAILURE",
                notificationType, recipient, message);
    }
}
