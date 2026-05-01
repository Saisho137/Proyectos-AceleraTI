package exercise1_notifications;

import exercise1_notifications.refactored.*;
import exercise1_notifications.refactored.EmailNotificationSender;
import exercise1_notifications.refactored.NotificationSender;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("refactoring")
@ExtendWith(MockitoExtension.class)
@DisplayName("Exercise 1: Notification Service - Strategy & Factory Pattern")
public class NotificationServiceRefactoredTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // PARTE 1: Tests para la interfaz NotificationSender (Strategy Pattern)
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("1.1 - NotificationSender Interface (Strategy Pattern)")
    class NotificationSenderStrategyTests {

        @Test
        @DisplayName("EmailNotificationSender debe implementar NotificationSender")
        void emailSenderShouldImplementNotificationSender() {
            // Arrange & Act
            NotificationSender sender = new EmailNotificationSender();

            // Assert - Verifica que es una instancia de la interfaz
            assertThat(sender).isInstanceOf(NotificationSender.class);
            assertThat(sender.getType()).isEqualTo("EMAIL");
        }

        @Test
        @DisplayName("SmsNotificationSender debe implementar NotificationSender")
        void smsSenderShouldImplementNotificationSender() {
            NotificationSender sender = new SmsNotificationSender();

            assertThat(sender).isInstanceOf(NotificationSender.class);
            assertThat(sender.getType()).isEqualTo("SMS");
        }

        @Test
        @DisplayName("PushNotificationSender debe implementar NotificationSender")
        void pushSenderShouldImplementNotificationSender() {
            NotificationSender sender = new PushNotificationSender();

            assertThat(sender).isInstanceOf(NotificationSender.class);
            assertThat(sender.getType()).isEqualTo("PUSH");
        }
    }
    //

    /// / ═══════════════════════════════════════════════════════════════════════════
    /// / PARTE 2: Tests para EmailNotificationSender
    /// / ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("1.2 - EmailNotificationSender")
    class EmailNotificationSenderTests {

        private EmailNotificationSender emailSender;

        @BeforeEach
        void setUp() {
            emailSender = new EmailNotificationSender();
        }

        @Test
        @DisplayName("Debe enviar email exitosamente con datos válidos")
        void shouldSendEmailSuccessfully() {
            // Act
            NotificationResult result = emailSender.send("user@example.com", "Hello World");

            // Assert
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getMessage()).contains("email", "sent");
        }

        @Test
        @DisplayName("Debe fallar si el email no contiene @")
        void shouldFailWithInvalidEmail() {
            NotificationResult result = emailSender.send("invalid-email", "Hello");

            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getMessage()).containsIgnoringCase("invalid");
        }

        @Test
        @DisplayName("Debe validar el recipient antes de enviar")
        void shouldValidateRecipient() {
            assertThat(emailSender.validateRecipient("test@test.com")).isTrue();
            assertThat(emailSender.validateRecipient("invalid")).isFalse();
            assertThat(emailSender.validateRecipient("")).isFalse();
            assertThat(emailSender.validateRecipient(null)).isFalse();
        }

        @Test
        @DisplayName("Debe formatear el mensaje como HTML")
        void shouldFormatMessageAsHtml() {
            String formatted = emailSender.formatMessage("Test message");

            assertThat(formatted).contains("<html>", "</html>");
            assertThat(formatted).contains("Test message");
        }
    }

    /// / ═══════════════════════════════════════════════════════════════════════════
    /// / PARTE 3: Tests para SmsNotificationSender
    /// / ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("1.3 - SmsNotificationSender")
    class SmsNotificationSenderTests {

        private SmsNotificationSender smsSender;

        @BeforeEach
        void setUp() {
            smsSender = new SmsNotificationSender();
        }

        @Test
        @DisplayName("Debe enviar SMS exitosamente con número válido")
        void shouldSendSmsSuccessfully() {
            NotificationResult result = smsSender.send("+1234567890", "Hello");

            assertThat(result.isSuccess()).isTrue();
        }

        @Test
        @DisplayName("Debe fallar con número de teléfono inválido")
        void shouldFailWithInvalidPhoneNumber() {
            NotificationResult result = smsSender.send("123", "Hello");

            assertThat(result.isSuccess()).isFalse();
        }

        @Test
        @DisplayName("Debe truncar mensajes mayores a 160 caracteres")
        void shouldTruncateLongMessages() {
            String longMessage = "A".repeat(200);
            String formatted = smsSender.formatMessage(longMessage);

            assertThat(formatted.length()).isLessThanOrEqualTo(160);
            assertThat(formatted).endsWith("...");
        }

        @Test
        @DisplayName("Debe validar formato de número telefónico")
        void shouldValidatePhoneNumber() {
            assertThat(smsSender.validateRecipient("+1234567890")).isTrue();
            assertThat(smsSender.validateRecipient("1234567890123")).isTrue();
            assertThat(smsSender.validateRecipient("123")).isFalse();
            assertThat(smsSender.validateRecipient("abc")).isFalse();
        }
    }

    /// / ═══════════════════════════════════════════════════════════════════════════
    /// / PARTE 4: Tests para PushNotificationSender
    /// / ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("1.4 - PushNotificationSender")
    class PushNotificationSenderTests {

        private PushNotificationSender pushSender;

        @BeforeEach
        void setUp() {
            pushSender = new PushNotificationSender();
        }

        @Test
        @DisplayName("Debe enviar push notification exitosamente")
        void shouldSendPushSuccessfully() {
            NotificationResult result = pushSender.send("device-token-12345", "Hello");

            assertThat(result.isSuccess()).isTrue();
        }

        @Test
        @DisplayName("Debe fallar con token de dispositivo muy corto")
        void shouldFailWithShortToken() {
            NotificationResult result = pushSender.send("short", "Hello");

            assertThat(result.isSuccess()).isFalse();
        }

        @Test
        @DisplayName("Debe truncar mensajes mayores a 100 caracteres")
        void shouldTruncateLongMessages() {
            String longMessage = "B".repeat(150);
            String formatted = pushSender.formatMessage(longMessage);

            assertThat(formatted.length()).isLessThanOrEqualTo(100);
        }
    }

    /// / ═══════════════════════════════════════════════════════════════════════════
    /// / PARTE 5: Tests para NotificationSenderFactory (Factory Pattern)
    /// / ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("1.5 - NotificationSenderFactory (Factory Pattern)")
    class NotificationSenderFactoryTests {

        private NotificationSenderFactory factory;

        @BeforeEach
        void setUp() {
            factory = new NotificationSenderFactory();
        }

        @Test
        @DisplayName("Debe crear EmailNotificationSender para tipo EMAIL")
        void shouldCreateEmailSender() {
            NotificationSender sender = factory.createSender("EMAIL");

            assertThat(sender).isInstanceOf(EmailNotificationSender.class);
        }

        @Test
        @DisplayName("Debe crear SmsNotificationSender para tipo SMS")
        void shouldCreateSmsSender() {
            NotificationSender sender = factory.createSender("SMS");

            assertThat(sender).isInstanceOf(SmsNotificationSender.class);
        }

        @Test
        @DisplayName("Debe crear PushNotificationSender para tipo PUSH")
        void shouldCreatePushSender() {
            NotificationSender sender = factory.createSender("PUSH");

            assertThat(sender).isInstanceOf(PushNotificationSender.class);
        }

        @Test
        @DisplayName("Debe ser case-insensitive")
        void shouldBeCaseInsensitive() {
            assertThat(factory.createSender("email")).isInstanceOf(EmailNotificationSender.class);
            assertThat(factory.createSender("Email")).isInstanceOf(EmailNotificationSender.class);
            assertThat(factory.createSender("EMAIL")).isInstanceOf(EmailNotificationSender.class);
        }

        @Test
        @DisplayName("Debe lanzar excepción para tipo desconocido")
        void shouldThrowExceptionForUnknownType() {
            assertThatThrownBy(() -> factory.createSender("UNKNOWN"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unknown notification type");
        }

        @Test
        @DisplayName("Debe soportar registrar nuevos tipos de sender (OCP)")
        void shouldSupportRegisteringNewSenders() {
            // Este test verifica que la factory sea extensible (OCP)
            factory.registerSender("SLACK", () -> new NotificationSender() {
                @Override
                public NotificationResult send(String recipient, String message) {
                    return NotificationResult.success("Slack sent");
                }

                @Override
                public boolean validateRecipient(String recipient) {
                    return recipient != null && recipient.startsWith("#");
                }

                @Override
                public String formatMessage(String message) {
                    return message;
                }

                @Override
                public String getType() {
                    return "SLACK";
                }
            });

            NotificationSender slackSender = factory.createSender("SLACK");
            assertThat(slackSender.getType()).isEqualTo("SLACK");
        }
    }

    /// / ═══════════════════════════════════════════════════════════════════════════
    /// / PARTE 6: Tests para NotificationResult (Value Object)
    /// / ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("1.6 - NotificationResult (Value Object)")
    class NotificationResultTests {

        @Test
        @DisplayName("Debe crear resultado exitoso")
        void shouldCreateSuccessResult() {
            NotificationResult result = NotificationResult.success("Sent successfully");

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getMessage()).isEqualTo("Sent successfully");
        }

        @Test
        @DisplayName("Debe crear resultado fallido")
        void shouldCreateFailureResult() {
            NotificationResult result = NotificationResult.failure("Invalid recipient");

            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getMessage()).isEqualTo("Invalid recipient");
        }
    }

    /// / ═══════════════════════════════════════════════════════════════════════════
    /// / PARTE 7: Tests para NotificationService Refactorizado (Orquestador)
    /// / ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("1.7 - NotificationService Refactorizado (Con Inyección de Dependencias)")
    class RefactoredNotificationServiceTests {

        @Mock
        private NotificationSender mockSender;

        @Mock
        private NotificationSenderFactory mockFactory;

        @Mock
        private NotificationLogger mockLogger;

        private NotificationService service;

        @BeforeEach
        void setUp() {
            // El servicio refactorizado debe recibir sus dependencias por constructor (DIP)
            service = new NotificationService(mockFactory, mockLogger);
        }

        @Test
        @DisplayName("Debe usar la factory para obtener el sender correcto")
        void shouldUseFactoryToGetSender() {
            // Arrange
            when(mockFactory.createSender("EMAIL")).thenReturn(mockSender);
            when(mockSender.send("test@test.com", "Hello"))
                    .thenReturn(NotificationResult.success("Sent"));

            // Act
            service.sendNotification("EMAIL", "test@test.com", "Hello");

            // Assert
            verify(mockFactory).createSender("EMAIL");
            verify(mockSender).send("test@test.com", "Hello");
        }

        @Test
        @DisplayName("Debe loggear las notificaciones enviadas")
        void shouldLogNotifications() {
            // Arrange
            when(mockFactory.createSender("SMS")).thenReturn(mockSender);
            when(mockSender.send(anyString(), anyString()))
                    .thenReturn(NotificationResult.success("Sent"));

            // Act
            service.sendNotification("SMS", "+1234567890", "Test");

            // Assert
            verify(mockLogger).log(any(NotificationLogEntry.class));
        }

        @Test
        @DisplayName("Debe retornar el resultado del sender")
        void shouldReturnSenderResult() {
            // Arrange
            NotificationResult expectedResult = NotificationResult.success("Email sent");
            when(mockFactory.createSender("EMAIL")).thenReturn(mockSender);
            when(mockSender.send(anyString(), anyString())).thenReturn(expectedResult);

            // Act
            NotificationResult result = service.sendNotification("EMAIL", "a@b.com", "Hi");

            // Assert
            assertThat(result).isEqualTo(expectedResult);
        }

        @Test
        @DisplayName("Debe manejar excepciones de la factory gracefully")
        void shouldHandleFactoryExceptions() {
            // Arrange
            when(mockFactory.createSender("INVALID"))
                    .thenThrow(new IllegalArgumentException("Unknown type"));

            // Act
            NotificationResult result = service.sendNotification("INVALID", "test", "msg");

            // Assert
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getMessage()).containsIgnoringCase("unknown");
        }
    }
}
