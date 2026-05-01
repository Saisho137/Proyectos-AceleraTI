package exercise3_reports;

import exercise3_reports.refactored.ReportService;
import exercise3_reports.refactored.formatter.*;
import exercise3_reports.refactored.exporter.*;
import exercise3_reports.refactored.model.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("refactoring")
@ExtendWith(MockitoExtension.class)
@DisplayName("Exercise 3: Report Generator - ISP & Adapter Pattern")
public class ReportGeneratorRefactoredTest {
    
    // ═══════════════════════════════════════════════════════════════════════════
    // PARTE 1: Tests para el Modelo de Datos
    // ═══════════════════════════════════════════════════════════════════════════
    
     @Nested
     @DisplayName("3.1 - ReportData Model")
     class ReportDataTests {

         @Test
         @DisplayName("ReportData debe contener título y filas de datos")
         void reportDataShouldContainTitleAndRows() {
             List<Map<String, Object>> rows = List.of(
                     Map.of("name", "John", "age", 30),
                     Map.of("name", "Jane", "age", 25)
             );

             ReportData reportData = new ReportData("Users Report", rows);

             assertThat(reportData.getTitle()).isEqualTo("Users Report");
             assertThat(reportData.getRows()).hasSize(2);
             assertThat(reportData.getColumns()).containsExactlyInAnyOrder("name", "age");
         }

         @Test
         @DisplayName("ReportData debe detectar columnas automáticamente")
         void reportDataShouldDetectColumns() {
             List<Map<String, Object>> rows = List.of(
                     Map.of("id", 1, "product", "Laptop", "price", 999.99)
             );

             ReportData reportData = new ReportData("Products", rows);

             assertThat(reportData.getColumns()).containsExactlyInAnyOrder("id", "product", "price");
         }

         @Test
         @DisplayName("ReportData vacío debe tener lista de columnas vacía")
         void emptyReportDataShouldHaveEmptyColumns() {
             ReportData reportData = new ReportData("Empty Report", List.of());

             assertThat(reportData.getRows()).isEmpty();
             assertThat(reportData.getColumns()).isEmpty();
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 2: Tests para ReportFormatter Interface (ISP)
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("3.2 - ReportFormatter Interface (ISP)")
     class ReportFormatterInterfaceTests {

         @Test
         @DisplayName("HtmlReportFormatter debe implementar ReportFormatter")
         void htmlFormatterShouldImplementInterface() {
             ReportFormatter formatter = new HtmlReportFormatter();

             assertThat(formatter).isInstanceOf(ReportFormatter.class);
             assertThat(formatter.getFormatType()).isEqualTo("HTML");
             assertThat(formatter.getFileExtension()).isEqualTo(".html");
         }

         @Test
         @DisplayName("CsvReportFormatter debe implementar ReportFormatter")
         void csvFormatterShouldImplementInterface() {
             ReportFormatter formatter = new CsvReportFormatter();

             assertThat(formatter).isInstanceOf(ReportFormatter.class);
             assertThat(formatter.getFormatType()).isEqualTo("CSV");
             assertThat(formatter.getFileExtension()).isEqualTo(".csv");
         }

         @Test
         @DisplayName("PdfReportFormatter debe implementar ReportFormatter")
         void pdfFormatterShouldImplementInterface() {
             ReportFormatter formatter = new PdfReportFormatter();

             assertThat(formatter).isInstanceOf(ReportFormatter.class);
             assertThat(formatter.getFormatType()).isEqualTo("PDF");
             assertThat(formatter.getFileExtension()).isEqualTo(".pdf");
         }

         @Test
         @DisplayName("ExcelReportFormatter debe implementar ReportFormatter")
         void excelFormatterShouldImplementInterface() {
             ReportFormatter formatter = new ExcelReportFormatter();

             assertThat(formatter).isInstanceOf(ReportFormatter.class);
             assertThat(formatter.getFormatType()).isEqualTo("EXCEL");
             assertThat(formatter.getFileExtension()).isEqualTo(".xlsx");
         }
     }

    //// ═══════════════════════════════════════════════════════════════════════════
    //// PARTE 3: Tests para HtmlReportFormatter
    //// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("3.3 - HtmlReportFormatter")
    class HtmlReportFormatterTests {

        private HtmlReportFormatter formatter;
        private ReportData sampleData;

        @BeforeEach
        void setUp() {
            formatter = new HtmlReportFormatter();
            sampleData = new ReportData("Test Report", List.of(
                    Map.of("name", "Alice", "score", 95),
                    Map.of("name", "Bob", "score", 87)
            ));
        }

        @Test
        @DisplayName("Debe generar HTML válido con estructura correcta")
        void shouldGenerateValidHtmlStructure() {
            String html = formatter.format(sampleData);

            assertThat(html).contains("<!DOCTYPE html>");
            assertThat(html).contains("<html>");
            assertThat(html).contains("</html>");
            assertThat(html).contains("<title>Test Report</title>");
        }

        @Test
        @DisplayName("Debe incluir tabla con headers")
        void shouldIncludeTableWithHeaders() {
            String html = formatter.format(sampleData);

            assertThat(html).contains("<table>");
            assertThat(html).contains("<th>");
            assertThat(html).containsIgnoringCase("name");
            assertThat(html).containsIgnoringCase("score");
        }

        @Test
        @DisplayName("Debe incluir datos en las filas")
        void shouldIncludeDataInRows() {
            String html = formatter.format(sampleData);

            assertThat(html).contains("<td>");
            assertThat(html).contains("Alice");
            assertThat(html).contains("Bob");
            assertThat(html).contains("95");
            assertThat(html).contains("87");
        }

        @Test
        @DisplayName("Debe manejar datos vacíos gracefully")
        void shouldHandleEmptyData() {
            ReportData emptyData = new ReportData("Empty", List.of());

            String html = formatter.format(emptyData);

            assertThat(html).contains("<!DOCTYPE html>");
            assertThat(html).containsIgnoringCase("no data");
        }
    }

    //// ═══════════════════════════════════════════════════════════════════════════
    //// PARTE 4: Tests para CsvReportFormatter
    //// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("3.4 - CsvReportFormatter")
    class CsvReportFormatterTests {

        private CsvReportFormatter formatter;

        @BeforeEach
        void setUp() {
            formatter = new CsvReportFormatter();
        }

        @Test
        @DisplayName("Debe generar CSV con headers en primera línea de datos")
        void shouldGenerateCsvWithHeaders() {
            ReportData data = new ReportData("Test", List.of(
                    Map.of("id", 1, "name", "Test")
            ));

            String csv = formatter.format(data);
            String[] lines = csv.split("\n");

            // Buscar la línea de headers (puede haber comentarios antes)
            boolean foundHeaders = false;
            for (String line : lines) {
                if (!line.startsWith("#") && !line.isBlank()) {
                    assertThat(line).containsIgnoringCase("id");
                    assertThat(line).containsIgnoringCase("name");
                    foundHeaders = true;
                    break;
                }
            }
            assertThat(foundHeaders).isTrue();
        }

        @Test
        @DisplayName("Debe escapar comas en valores")
        void shouldEscapeCommasInValues() {
            ReportData data = new ReportData("Test", List.of(
                    Map.of("description", "Hello, World")
            ));

            String csv = formatter.format(data);

            // El valor con coma debe estar entre comillas
            assertThat(csv).contains("\"Hello, World\"");
        }

        @Test
        @DisplayName("Debe separar valores con coma")
        void shouldSeparateValuesWithComma() {
            ReportData data = new ReportData("Test", List.of(
                    Map.of("a", "1", "b", "2", "c", "3")
            ));

            String csv = formatter.format(data);

            // Debe haber comas separando los valores
            assertThat(csv).contains(",");
        }
    }

    //// ═══════════════════════════════════════════════════════════════════════════
    //// PARTE 5: Tests para ReportExporter Interface (ISP)
    //// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("3.5 - ReportExporter Interface (ISP)")
    class ReportExporterInterfaceTests {

        @Test
        @DisplayName("FileReportExporter debe implementar ReportExporter")
        void fileExporterShouldImplementInterface() {
            ReportExporter exporter = new FileReportExporter("/tmp");

            assertThat(exporter).isInstanceOf(ReportExporter.class);
        }

        @Test
        @DisplayName("EmailReportExporter debe implementar ReportExporter")
        void emailExporterShouldImplementInterface() {
            ReportExporter exporter = new EmailReportExporter();

            assertThat(exporter).isInstanceOf(ReportExporter.class);
        }
    }

    //// ═══════════════════════════════════════════════════════════════════════════
    //// PARTE 6: Tests para FileReportExporter
    //// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("3.6 - FileReportExporter")
    class FileReportExporterTests {

        @Test
        @DisplayName("Debe exportar contenido y retornar resultado exitoso")
        void shouldExportAndReturnSuccess() {
            FileReportExporter exporter = new FileReportExporter("/tmp");

            ExportResult result = exporter.export("test content", "test_report", ".txt");

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getDestination()).contains("test_report");
        }
    }

    //// ═══════════════════════════════════════════════════════════════════════════
    //// PARTE 7: Tests para EmailReportExporter
    //// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("3.7 - EmailReportExporter")
    class EmailReportExporterTests {

        @Test
        @DisplayName("Debe enviar a email válido")
        void shouldSendToValidEmail() {
            EmailReportExporter exporter = new EmailReportExporter();

            ExportResult result = exporter.export("test content", "Report", "user@example.com");

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getDestination()).isEqualTo("user@example.com");
        }

        @Test
        @DisplayName("Debe fallar con email vacio")
        void shouldFailWithInvalidEmail() {
            EmailReportExporter exporter = new EmailReportExporter();

            ExportResult result = exporter.export("content", "Report", "");

            assertThat(result.isSuccess()).isFalse();
        }
    }

    //// ═══════════════════════════════════════════════════════════════════════════
    //// PARTE 8: Tests para ReportFormatterFactory
    //// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("3.8 - ReportFormatterFactory")
    class ReportFormatterFactoryTests {

        private ReportFormatterFactory factory;

        @BeforeEach
        void setUp() {
            factory = new ReportFormatterFactory();
        }

        @Test
        @DisplayName("Debe crear formatter correcto para cada tipo")
        void shouldCreateCorrectFormatterForEachType() {
            assertThat(factory.createFormatter("HTML")).isInstanceOf(HtmlReportFormatter.class);
            assertThat(factory.createFormatter("CSV")).isInstanceOf(CsvReportFormatter.class);
            assertThat(factory.createFormatter("PDF")).isInstanceOf(PdfReportFormatter.class);
            assertThat(factory.createFormatter("EXCEL")).isInstanceOf(ExcelReportFormatter.class);
        }

        @Test
        @DisplayName("Debe ser case-insensitive")
        void shouldBeCaseInsensitive() {
            assertThat(factory.createFormatter("html")).isInstanceOf(HtmlReportFormatter.class);
            assertThat(factory.createFormatter("Html")).isInstanceOf(HtmlReportFormatter.class);
        }

        @Test
        @DisplayName("Debe lanzar excepción para formato desconocido")
        void shouldThrowExceptionForUnknownFormat() {
            assertThatThrownBy(() -> factory.createFormatter("UNKNOWN"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unsupported format");
        }

        @Test
        @DisplayName("Debe listar formatos soportados")
        void shouldListSupportedFormats() {
            List<String> formats = factory.getSupportedFormats();

            assertThat(formats).containsExactlyInAnyOrder("HTML", "CSV", "PDF", "EXCEL");
        }

        @Test
        @DisplayName("Debe permitir registrar nuevos formatters (OCP)")
        void shouldAllowRegisteringNewFormatters() {
            factory.registerFormatter("JSON", () -> new ReportFormatter() {
                @Override
                public String format(ReportData data) {
                    return "{\"title\": \"" + data.getTitle() + "\"}";
                }

                @Override
                public String getFormatType() {
                    return "JSON";
                }

                @Override
                public String getFileExtension() {
                    return ".json";
                }
            });

            ReportFormatter jsonFormatter = factory.createFormatter("JSON");
            assertThat(jsonFormatter.getFormatType()).isEqualTo("JSON");
        }
    }

    //// ═══════════════════════════════════════════════════════════════════════════
    //// PARTE 10: Tests para ReportService (Orquestador con DIP)
    //// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("3.10 - ReportService (Orquestador)")
    class ReportServiceTests {

        @Mock
        private ReportFormatterFactory mockFactory;

        @Mock
        private ReportFormatter mockFormatter;

        @Mock
        private ReportExporter mockExporter;

        private ReportService service;

        @BeforeEach
        void setUp() {
            service = new ReportService(mockFactory);
        }

        @Test
        @DisplayName("Debe usar factory para obtener formatter")
        void shouldUseFactoryToGetFormatter() {
            ReportData data = new ReportData("Test", List.of(Map.of("a", 1)));
            when(mockFactory.createFormatter("HTML")).thenReturn(mockFormatter);
            when(mockFormatter.format(data)).thenReturn("<html></html>");

            service.generate("HTML", data);

            verify(mockFactory).createFormatter("HTML");
            verify(mockFormatter).format(data);
        }

        @Test
        @DisplayName("Debe generar y exportar reporte")
        void shouldGenerateAndExport() {
            ReportData data = new ReportData("Test", List.of(Map.of("x", 1)));
            when(mockFactory.createFormatter("CSV")).thenReturn(mockFormatter);
            when(mockFormatter.format(data)).thenReturn("x\n1");
            when(mockExporter.export(anyString(), anyString(), anyString()))
                    .thenReturn(ExportResult.success("/path/to/file.csv"));

            ExportResult result = service.generateAndExport("CSV", data, "report", ".csv", mockExporter);

            verify(mockExporter).export("x\n1", "report", ".csv");
            assertThat(result.isSuccess()).isTrue();
        }
    }
}
