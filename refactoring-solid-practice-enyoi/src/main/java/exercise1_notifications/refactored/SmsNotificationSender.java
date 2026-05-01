package exercise1_notifications.refactored;

public class SmsNotificationSender implements  NotificationSender {
    @Override
    public String getType() {
        return "SMS";
    }

    @Override
    public NotificationResult send(String recipient, String message) {
        if (!validateRecipient(recipient)) {
            return NotificationResult.failure("Invalid notification recipient");
        }

        String formattedMessage = formatMessage(message);

        return NotificationResult.success("sms to " + recipient + " : " + formattedMessage + " sent");
    }

    @Override
    public boolean validateRecipient(String recipient) {
        return recipient.matches("\\+?\\d{10,15}");
    }

    @Override
    public String formatMessage(String message) {
        if (message.length() > 160) {
            return message.substring(0, 157) + "...";
        }
        return message;
    }
}
