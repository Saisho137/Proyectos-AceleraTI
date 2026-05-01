package exercise3_reports.refactored.formatter;

import exercise3_reports.refactored.model.ReportData;

import java.time.LocalDateTime;
import java.util.List;

public class ExcelReportFormatter implements ReportFormatter {
    @Override
    public String getFormatType() {
        return "EXCEL";
    }

    @Override
    public String getFileExtension() {
        return ".xlsx";
    }

    @Override
    public String format(ReportData reportData) {
        // Simulación de Excel (en realidad necesitaría Apache POI)
        StringBuilder excel = new StringBuilder();
        excel.append("=== EXCEL FORMAT (Simulated) ===\n");
        excel.append("Sheet: ").append(reportData.getTitle()).append("\n");
        excel.append("Generated: ").append(LocalDateTime.now()).append("\n\n");

        if (reportData.getRows() != null && !reportData.getRows().isEmpty()) {
            // Headers in row 1
            excel.append("Row 1 (Headers): ");
            excel.append(String.join(" | ", reportData.getColumns())).append("\n");

            // Data rows
            int rowNum = 2;
            for (List<Object> row : reportData.getRows()) {
                excel.append("Row ").append(rowNum++).append(": ");
                StringBuilder rowStr = new StringBuilder();
                boolean first = true;
                for (Object value : row) {
                    if (!first) rowStr.append(" | ");
                    rowStr.append(value);
                    first = false;
                }
                excel.append(rowStr).append("\n");
            }
        }

        return excel.toString();
    }
}
