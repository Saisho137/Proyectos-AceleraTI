package exercise3_reports.refactored;

import exercise3_reports.refactored.exporter.ReportExporter;
import exercise3_reports.refactored.formatter.ReportFormatter;
import exercise3_reports.refactored.formatter.ReportFormatterFactory;
import exercise3_reports.refactored.model.ExportResult;
import exercise3_reports.refactored.model.ReportData;

public class ReportService {
    private final ReportFormatterFactory reportFormatterFactory;
    private String contentGenerated;

    public ReportService(ReportFormatterFactory reportFormatterFactory) {
        this.reportFormatterFactory = reportFormatterFactory;
        this.contentGenerated = "";
    }

    public void generate(String type, ReportData data) {
        try {
            ReportFormatter formatter = reportFormatterFactory.createFormatter(type);
            contentGenerated = formatter.format(data);
        } catch (Exception e) {
            System.out.println("Error generating report");
        }
    }

    public ExportResult generateAndExport(String type, ReportData data, String title, String destination, ReportExporter exporter) {
        generate(type, data);

        if (contentGenerated.isEmpty()) {
            System.out.println("Can't export");
            return ExportResult.failed(destination);
        }

        return exporter.export(contentGenerated, title, destination);
    }
}
