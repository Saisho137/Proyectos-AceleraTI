package exercise1_notifications.refactored;

public class PushNotificationSender implements  NotificationSender {
    @Override
    public String getType() {
        return "PUSH";
    }

    @Override
    public NotificationResult send(String recipient, String message) {
        if (!validateRecipient(recipient)) {
            return  NotificationResult.failure("Invalid notification recipient");
        }

        String formattedMessage = formatMessage(message);

        return NotificationResult.success("push to device " + recipient + " : " + formattedMessage + " sent");
    }

    @Override
    public boolean validateRecipient(String recipient) {
        return !(recipient.length() < 10);
    }

    @Override
    public String formatMessage(String message) {
        if (message.length() > 100) {
            return message.substring(0, 97) + "...";
        }
        return message;
    }
}
