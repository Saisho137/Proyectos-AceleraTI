package exercise3_reports.refactored.model;

public class ExportResult {
    private final boolean success;
    private final String destination;

    public ExportResult(boolean success, String destination) {
        this.success = success;
        this.destination = destination;
    }

    public static ExportResult success(String destination) {
        return new ExportResult(true, destination);
    }

    public static ExportResult failed(String destination) {
        return new ExportResult(false, destination);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getDestination() {
        return destination;
    }
}
