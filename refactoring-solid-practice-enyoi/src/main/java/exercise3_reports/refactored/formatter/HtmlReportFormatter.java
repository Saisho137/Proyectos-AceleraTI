package exercise3_reports.refactored.formatter;

import exercise3_reports.refactored.model.ReportData;

import java.time.LocalDateTime;
import java.util.List;

public class HtmlReportFormatter implements ReportFormatter {
    @Override
    public String getFormatType() {
        return "HTML";
    }

    @Override
    public String getFileExtension() {
        return ".html";
    }

    @Override
    public String format(ReportData reportData) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<title>").append(reportData.getTitle()).append("</title>\n");
        html.append("<style>")
                .append("table { border-collapse: collapse; width: 100%; }")
                .append("th, td { border: 1px solid black; padding: 8px; }")
                .append("th { background-color: #f2f2f2; }")
                .append("</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<h1>").append(reportData.getTitle()).append("</h1>\n");
        html.append("<p>Generated: ").append(LocalDateTime.now()).append("</p>\n");

        if (reportData.getRows() != null && !reportData.getRows().isEmpty()) {
            html.append("<table>\n<tr>\n");

            // Headers
            for (String key : reportData.getColumns()) {
                html.append("<th>").append(key).append("</th>");
            }
            html.append("</tr>\n");

            // Data rows
            for (List<Object> row : reportData.getRows()) {
                html.append("<tr>");
                for (Object value : row) {
                    html.append("<td>").append(value).append("</td>");
                }
                html.append("</tr>\n");
            }
            html.append("</table>\n");
        } else {
            html.append("<p>No data available</p>\n");
        }

        html.append("</body>\n</html>");
        return html.toString();
    }
}
