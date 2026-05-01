package exercise3_reports.refactored.exporter;

import exercise3_reports.refactored.model.ExportResult;

public class EmailReportExporter implements ReportExporter {
    @Override
    public ExportResult export(String content, String title, String destination) {
        if (destination.isEmpty()) {
            return ExportResult.failed("invalid_destination");
        }
        System.out.println("Sending report '" + title + "' to " + destination);
        System.out.println("Content length: " + content.length() + " chars");
        return ExportResult.success(destination);
    }
}
