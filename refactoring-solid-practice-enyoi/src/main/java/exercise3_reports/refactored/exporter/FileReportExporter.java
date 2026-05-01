package exercise3_reports.refactored.exporter;

import exercise3_reports.refactored.model.ExportResult;

public class FileReportExporter implements ReportExporter {
    private final String folderPath;

    public FileReportExporter(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public ExportResult export(String content, String title, String destination) {
        if (destination.isEmpty()) {
            return ExportResult.failed("invalid_destination");
        }
        // Simulación de guardado
        String completeDestination = getFilePath(title, destination);
        System.out.println("Saving report to " + completeDestination);
        System.out.println("Content length: " + content.length() + " chars");
        return ExportResult.success(completeDestination);
    }

    private String getFilePath(String fileName, String extension) {
        return folderPath + "/" + fileName + "." + extension;
    }
}
