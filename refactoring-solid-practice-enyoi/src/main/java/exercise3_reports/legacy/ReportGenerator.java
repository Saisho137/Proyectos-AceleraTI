package exercise3_reports.legacy;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    
    /**
     * Genera un reporte en el formato especificado.
     * 
     * PROBLEMAS:
     * - Switch gigante (viola OCP)
     * - Múltiples responsabilidades en un solo método
     * - Imposible de testear unitariamente cada formato
     */
    public String generateReport(String format, String title, List<Map<String, Object>> data) {
        String content;
        
        switch (format.toUpperCase()) {
            case "HTML":
                content = generateHtmlReport(title, data);
                break;
            case "CSV":
                content = generateCsvReport(title, data);
                break;
            case "PDF":
                content = generatePdfReport(title, data);
                break;
            case "EXCEL":
                content = generateExcelReport(title, data);
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
        
        return content;
    }
    
    /**
     * Genera y envía el reporte (mezcla generación con envío - viola SRP)
     */
    public void generateAndSendReport(String format, String title, 
                                       List<Map<String, Object>> data, String email) {
        String content = generateReport(format, title, data);
        sendByEmail(content, email, title);
    }
    
    /**
     * Genera y guarda el reporte (mezcla generación con I/O - viola SRP)
     */
    public void generateAndSaveReport(String format, String title,
                                       List<Map<String, Object>> data, String filePath) {
        String content = generateReport(format, title, data);
        saveToFile(content, filePath);
    }
    
    // ========== GENERADORES DE FORMATO (cada uno debería ser su propia clase) ==========
    
    private String generateHtmlReport(String title, List<Map<String, Object>> data) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<title>").append(title).append("</title>\n");
        html.append("<style>")
            .append("table { border-collapse: collapse; width: 100%; }")
            .append("th, td { border: 1px solid black; padding: 8px; }")
            .append("th { background-color: #f2f2f2; }")
            .append("</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<h1>").append(title).append("</h1>\n");
        html.append("<p>Generated: ").append(LocalDateTime.now()).append("</p>\n");
        
        if (data != null && !data.isEmpty()) {
            html.append("<table>\n<tr>\n");
            
            // Headers
            for (String key : data.get(0).keySet()) {
                html.append("<th>").append(key).append("</th>");
            }
            html.append("</tr>\n");
            
            // Data rows
            for (Map<String, Object> row : data) {
                html.append("<tr>");
                for (Object value : row.values()) {
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
    
    private String generateCsvReport(String title, List<Map<String, Object>> data) {
        StringBuilder csv = new StringBuilder();
        csv.append("# ").append(title).append("\n");
        csv.append("# Generated: ").append(LocalDateTime.now()).append("\n\n");
        
        if (data != null && !data.isEmpty()) {
            // Headers
            csv.append(String.join(",", data.get(0).keySet())).append("\n");
            
            // Data rows
            for (Map<String, Object> row : data) {
                StringBuilder rowStr = new StringBuilder();
                boolean first = true;
                for (Object value : row.values()) {
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
    
    private String generatePdfReport(String title, List<Map<String, Object>> data) {
        // Simulación de PDF (en realidad necesitaría una librería como iText)
        StringBuilder pdf = new StringBuilder();
        pdf.append("%PDF-1.4 (Simulated)\n");
        pdf.append("Title: ").append(title).append("\n");
        pdf.append("Generated: ").append(LocalDateTime.now()).append("\n");
        pdf.append("─────────────────────────────────────────\n");
        
        if (data != null && !data.isEmpty()) {
            for (Map<String, Object> row : data) {
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    pdf.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                pdf.append("─────────────────────────────────────────\n");
            }
        }
        
        return pdf.toString();
    }
    
    private String generateExcelReport(String title, List<Map<String, Object>> data) {
        // Simulación de Excel (en realidad necesitaría Apache POI)
        StringBuilder excel = new StringBuilder();
        excel.append("=== EXCEL FORMAT (Simulated) ===\n");
        excel.append("Sheet: ").append(title).append("\n");
        excel.append("Generated: ").append(LocalDateTime.now()).append("\n\n");
        
        if (data != null && !data.isEmpty()) {
            // Headers in row 1
            excel.append("Row 1 (Headers): ");
            excel.append(String.join(" | ", data.get(0).keySet())).append("\n");
            
            // Data rows
            int rowNum = 2;
            for (Map<String, Object> row : data) {
                excel.append("Row ").append(rowNum++).append(": ");
                StringBuilder rowStr = new StringBuilder();
                boolean first = true;
                for (Object value : row.values()) {
                    if (!first) rowStr.append(" | ");
                    rowStr.append(value);
                    first = false;
                }
                excel.append(rowStr).append("\n");
            }
        }
        
        return excel.toString();
    }
    
    // ========== EXPORTADORES (deberían estar separados - viola SRP) ==========
    
    private void sendByEmail(String content, String email, String title) {
        // Simulación de envío de email
        System.out.println(" Sending report '" + title + "' to " + email);
        System.out.println("Content length: " + content.length() + " chars");
        // En realidad aquí iría la lógica de envío de email
    }
    
    private void saveToFile(String content, String filePath) {
        // Simulación de guardado
        System.out.println(" Saving report to " + filePath);
        System.out.println("Content length: " + content.length() + " chars");
        
        // En un escenario real:
        // try (FileWriter writer = new FileWriter(filePath)) {
        //     writer.write(content);
        // } catch (IOException e) {
        //     throw new RuntimeException("Failed to save report", e);
        // }
    }
}
