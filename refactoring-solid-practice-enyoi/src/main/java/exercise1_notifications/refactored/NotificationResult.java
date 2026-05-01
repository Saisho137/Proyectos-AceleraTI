package exercise1_notifications.refactored;

public class NotificationResult {
    private final boolean isSuccess;
    private final String message;

    public NotificationResult(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public static NotificationResult success(String message) {
        return new NotificationResult(true, message);
    }

    public static NotificationResult failure(String message) {
        return new NotificationResult(false, message);
    }
}
