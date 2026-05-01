package exercise1_notifications.refactored;

public class NotificationService {
    private final NotificationLogger logger;
    private final NotificationSenderFactory factory;

    public NotificationService(NotificationSenderFactory factory, NotificationLogger logger) {
        this.logger = logger;
        this.factory = factory;
    }

    public NotificationResult sendNotification(String notificationType, String recipient, String message) {
        NotificationResult result = null;

        try {
            NotificationSender sender = factory.createSender(notificationType);
            result = sender.send(recipient, message);

            logger.log(new NotificationLogEntry(
                    result.isSuccess(),
                    notificationType,
                    recipient,
                    message
            ));

        } catch (Exception e) {
            logger.log(new NotificationLogEntry(
                    false,
                    notificationType,
                    recipient,
                    message
            ));
            result = NotificationResult.failure(e.getMessage());
        }

        return result;
    }
}
