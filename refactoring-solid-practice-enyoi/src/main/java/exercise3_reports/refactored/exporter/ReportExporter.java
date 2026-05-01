package exercise3_reports.refactored.exporter;

import exercise3_reports.refactored.model.ExportResult;

public interface ReportExporter {
    ExportResult export(String content, String title, String destination);
}
