package exercise1_notifications.refactored;

public class Main {
    public static void main(String[] args) {
        /*NotificationService legacyService = new NotificationService();
        legacyService.sendNotification("EMAIL","a@a.a", "Hello World!");*/

        NotificationSenderFactory factory = new NotificationSenderFactory();
        NotificationLogger logger = new NotificationLogger();


        NotificationService refactoredService = new NotificationService(
                factory,
                logger
        );

        refactoredService.sendNotification("EMAIL", "a@a.a", "Hello World!");

        logger.getNotificationLog().forEach(System.out::println);
    }
}
