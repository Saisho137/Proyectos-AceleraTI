package exercise1_notifications.refactored;

public class EmailNotificationSender implements NotificationSender {
    @Override
    public String getType() {
        return "EMAIL";
    }

    @Override
    public NotificationResult send(String recipient, String message) {
        if (!validateRecipient(recipient)) {
            return NotificationResult.failure("invalid notification recipient");
        }

        String messageFormatted = formatMessage(message);

        return NotificationResult.success("email to " + recipient + " : " + messageFormatted + " sent");
    }

    @Override
    public boolean validateRecipient(String recipient) {
        return (recipient != null && recipient.contains("@"));
    }

    @Override
    public String formatMessage(String message) {
        return "<html><body><h1>Notification</h1><p>" + message + "</p></body></html>";
    }
}
