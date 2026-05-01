package exercise3_reports.refactored.formatter;

import exercise3_reports.refactored.model.ReportData;

public interface ReportFormatter {
    public String getFormatType();

    public String getFileExtension();

    public String format(ReportData reportData);
}
