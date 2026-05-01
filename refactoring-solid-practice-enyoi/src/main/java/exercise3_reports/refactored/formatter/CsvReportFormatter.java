package exercise3_reports.refactored.formatter;

import exercise3_reports.refactored.model.ReportData;

import java.time.LocalDateTime;
import java.util.List;

public class CsvReportFormatter implements ReportFormatter {
    @Override
    public String getFormatType() {
        return "CSV";
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }

    @Override
    public String format(ReportData reportData) {
        StringBuilder csv = new StringBuilder();
        csv.append("# ").append(reportData.getTitle()).append("\n");
        csv.append("# Generated: ").append(LocalDateTime.now()).append("\n\n");

        if (reportData.getRows() != null && !reportData.getRows().isEmpty()) {
            // Headers
            csv.append(String.join(",", reportData.getColumns())).append("\n");

            // Data rows
            for (List<Object> row : reportData.getRows()) {
                StringBuilder rowStr = new StringBuilder();
                boolean first = true;
                for (Object value : row) {
                    if (!first) rowStr.append(",");
                    // Escape commas in values
                    String strValue = String.valueOf(value);
                    if (strValue.contains(",")) {
                        strValue = "\"" + strValue + "\"";
                    }
                    rowStr.append(strValue);
                    first = false;
                }
                csv.append(rowStr).append("\n");
            }
        }

        return csv.toString();
    }
}
