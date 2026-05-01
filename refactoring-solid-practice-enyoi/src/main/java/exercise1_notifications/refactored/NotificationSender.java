package exercise1_notifications.refactored;

public interface NotificationSender {
    String getType();

    NotificationResult send(String recipient, String message);

    boolean validateRecipient(String recipient);

    String formatMessage(String message);
}
