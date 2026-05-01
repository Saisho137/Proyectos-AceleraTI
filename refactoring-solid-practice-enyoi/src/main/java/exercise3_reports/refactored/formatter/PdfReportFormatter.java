package exercise3_reports.refactored.formatter;

import exercise3_reports.refactored.model.ReportData;

public class PdfReportFormatter implements ReportFormatter {
    @Override
    public String getFormatType() {
        return "PDF";
    }

    @Override
    public String getFileExtension() {
        return ".pdf";
    }

    @Override
    public String format(ReportData reportData) {
        return "";
    }
}
