package exercise3_reports.refactored.formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class ReportFormatterFactory {
    private final HashMap<String, Supplier<ReportFormatter>> reportFormatterSupplier;
    private final List<String> supportedFormats;

    public ReportFormatterFactory() {
        this.reportFormatterSupplier = new HashMap<>();
        this.supportedFormats = new ArrayList<>();

        supportedFormats.add("PDF");
        reportFormatterSupplier.put("PDF", PdfReportFormatter::new);
        supportedFormats.add("CSV");
        reportFormatterSupplier.put("CSV", CsvReportFormatter::new);
        supportedFormats.add("EXCEL");
        reportFormatterSupplier.put("EXCEL", ExcelReportFormatter::new);
        supportedFormats.add("HTML");
        reportFormatterSupplier.put("HTML", HtmlReportFormatter::new);
    }

    public ReportFormatter createFormatter(String type) {
        if (type == null || type.isEmpty()) throw new IllegalArgumentException("No se puede crear");

        Supplier<ReportFormatter> supplier = reportFormatterSupplier.get(type.toUpperCase());

        if (supplier == null) throw new IllegalArgumentException("Unsupported format");

        return supplier.get();
    }

    public void registerFormatter(String type, Supplier<ReportFormatter> supplier) {
        reportFormatterSupplier.put(type.toUpperCase(), supplier);
        supportedFormats.add(type.toUpperCase());
    }

    public List<String> getSupportedFormats() {
        return new ArrayList<>(supportedFormats);
    }
}
