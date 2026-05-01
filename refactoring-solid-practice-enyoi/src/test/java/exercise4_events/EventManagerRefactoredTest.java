package exercise4_events;

import exercise4_events.refactored.*;
import exercise4_events.refactored.event.*;
import exercise4_events.refactored.listener.*;
import exercise4_events.refactored.model.AuditLogEntry;
import exercise4_events.refactored.publisher.EventPublisher;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("refactoring")
@ExtendWith(MockitoExtension.class)
@DisplayName("Exercise 4: Event System - Observer Pattern")
public class EventManagerRefactoredTest {
    
    // ═══════════════════════════════════════════════════════════════════════════
    // PARTE 1: Tests para Event Classes (Modelo de Eventos)
    // ═══════════════════════════════════════════════════════════════════════════
    
     @Nested
     @DisplayName("4.1 - Event Classes")
     class EventClassesTests {

         @Test
         @DisplayName("Event debe tener timestamp y tipo")
         void eventShouldHaveTimestampAndType() {
             Event event = new UserRegisteredEvent("user@test.com", "John Doe");

             assertThat(event.getTimestamp()).isNotNull();
             assertThat(event.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
             assertThat(event.getEventType()).isEqualTo("USER_REGISTERED");
         }

         @Test
         @DisplayName("OrderPlacedEvent debe contener orderId, customerEmail y amount")
         void orderPlacedEventShouldContainOrderDetails() {
             OrderPlacedEvent event = new OrderPlacedEvent(
                     "ORD-123", "customer@test.com", "+1234567890", 299.99);

             assertThat(event.getEventType()).isEqualTo("ORDER_PLACED");
         }

         @Test
         @DisplayName("PaymentReceivedEvent debe contener paymentId y amount")
         void paymentReceivedEventShouldContainPaymentDetails() {
             PaymentReceivedEvent event = new PaymentReceivedEvent(
                     "PAY-456", "ORD-123", 299.99, "customer@test.com");

             assertThat(event.getEventType()).isEqualTo("PAYMENT_RECEIVED");
         }

         @Test
         @DisplayName("SystemAlertEvent debe contener level, message y component")
         void systemAlertEventShouldContainAlertDetails() {
             SystemAlertEvent event = new SystemAlertEvent(
                     AlertLevel.CRITICAL, "Database connection failed", "DatabaseService");

             assertThat(event.getEventType()).isEqualTo("SYSTEM_ALERT");
         }

         @Test
         @DisplayName("AlertLevel enum debe tener INFO, WARNING y CRITICAL")
         void alertLevelShouldHaveCorrectValues() {
             assertThat(AlertLevel.values())
                     .containsExactlyInAnyOrder(
                             AlertLevel.INFO,
                             AlertLevel.WARNING,
                             AlertLevel.CRITICAL);
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 2: Tests para EventListener Interface (Observer)
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("4.2 - EventListener Interface (Observer)")
     class EventListenerInterfaceTests {

         @Test
         @DisplayName("Todos los listeners deben implementar EventListener")
         void allListenersShouldImplementInterface() {
             assertThat(new EmailNotificationListener()).isInstanceOf(EventListener.class);
             assertThat(new SmsNotificationListener()).isInstanceOf(EventListener.class);
             assertThat(new SlackNotificationListener()).isInstanceOf(EventListener.class);
             assertThat(new DashboardUpdateListener()).isInstanceOf(EventListener.class);
             assertThat(new AuditLogListener()).isInstanceOf(EventListener.class);
         }

         @Test
         @DisplayName("EventListener debe tener método onEvent()")
         void eventListenerShouldHaveOnEventMethod() {
             EventListener listener = new EmailNotificationListener();
             Event event = new UserRegisteredEvent("test@test.com", "Test");

             // No debe lanzar excepción
             assertThatCode(() -> listener.onEvent(event)).doesNotThrowAnyException();
         }

         @Test
         @DisplayName("EventListener puede filtrar eventos con supports()")
         void eventListenerCanFilterEventsWithSupports() {
             EventListener listener = new EmailNotificationListener();

             // Debe soportar UserRegisteredEvent
             assertThat(listener.supports(UserRegisteredEvent.class)).isTrue();
             // Puede no soportar otros
             // La implementación decide qué eventos soporta
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 3: Tests para EmailNotificationListener
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("4.3 - EmailNotificationListener")
     class EmailNotificationListenerTests {

         private EmailNotificationListener emailListener;

         @BeforeEach
         void setUp() {
             emailListener = new EmailNotificationListener();
         }

         @Test
         @DisplayName("Debe manejar UserRegisteredEvent")
         void shouldHandleUserRegisteredEvent() {
             UserRegisteredEvent event = new UserRegisteredEvent("user@test.com", "John");

             assertThatCode(() -> emailListener.onEvent(event))
                     .doesNotThrowAnyException();
         }

         @Test
         @DisplayName("Debe manejar OrderPlacedEvent")
         void shouldHandleOrderPlacedEvent() {
             OrderPlacedEvent event = new OrderPlacedEvent(
                     "ORD-1", "user@test.com", null, 100.0);

             assertThatCode(() -> emailListener.onEvent(event))
                     .doesNotThrowAnyException();
         }

         @Test
         @DisplayName("Debe manejar PaymentReceivedEvent")
         void shouldHandlePaymentReceivedEvent() {
             PaymentReceivedEvent event = new PaymentReceivedEvent(
                     "PAY-1", "ORD-1", 100.0, "user@test.com");

             assertThatCode(() -> emailListener.onEvent(event))
                     .doesNotThrowAnyException();
         }

         @Test
         @DisplayName("Debe soportar eventos que requieren email")
         void shouldSupportEmailRelatedEvents() {
             assertThat(emailListener.supports(UserRegisteredEvent.class)).isTrue();
             assertThat(emailListener.supports(OrderPlacedEvent.class)).isTrue();
             assertThat(emailListener.supports(PaymentReceivedEvent.class)).isTrue();
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 4: Tests para SmsNotificationListener
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("4.4 - SmsNotificationListener")
     class SmsNotificationListenerTests {

         private SmsNotificationListener smsListener;

         @BeforeEach
         void setUp() {
             smsListener = new SmsNotificationListener();
         }

         @Test
         @DisplayName("Debe manejar OrderPlacedEvent si tiene teléfono")
         void shouldHandleOrderPlacedEventWithPhone() {
             OrderPlacedEvent event = new OrderPlacedEvent(
                     "ORD-1", "user@test.com", "+1234567890", 100.0);

             assertThatCode(() -> smsListener.onEvent(event))
                     .doesNotThrowAnyException();
         }

         @Test
         @DisplayName("Debe manejar SystemAlertEvent CRITICAL")
         void shouldHandleCriticalSystemAlert() {
             SystemAlertEvent event = new SystemAlertEvent(
                     AlertLevel.CRITICAL, "System down", "MainService");

             assertThatCode(() -> smsListener.onEvent(event))
                     .doesNotThrowAnyException();
         }

         @Test
         @DisplayName("Debe soportar eventos que requieren SMS urgente")
         void shouldSupportSmsRelatedEvents() {
             assertThat(smsListener.supports(OrderPlacedEvent.class)).isTrue();
             assertThat(smsListener.supports(SystemAlertEvent.class)).isTrue();
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 5: Tests para SlackNotificationListener
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("4.5 - SlackNotificationListener")
     class SlackNotificationListenerTests {

         private SlackNotificationListener slackListener;

         @BeforeEach
         void setUp() {
             slackListener = new SlackNotificationListener();
         }

         @Test
         @DisplayName("Debe soportar todos los tipos de eventos")
         void shouldSupportAllEventTypes() {
             assertThat(slackListener.supports(UserRegisteredEvent.class)).isTrue();
             assertThat(slackListener.supports(OrderPlacedEvent.class)).isTrue();
             assertThat(slackListener.supports(PaymentReceivedEvent.class)).isTrue();
             assertThat(slackListener.supports(SystemAlertEvent.class)).isTrue();
         }

         @Test
         @DisplayName("Debe obtener canal correcto para cada tipo de evento")
         void shouldGetCorrectChannelForEventType() {
             assertThat(slackListener.getChannelFor(
                     new UserRegisteredEvent("a@b.com", "Test")))
                     .isEqualTo("#new-users");

             assertThat(slackListener.getChannelFor(
                     new OrderPlacedEvent("O1", "a@b.com", null, 100)))
                     .isEqualTo("#orders");

             assertThat(slackListener.getChannelFor(
                     new SystemAlertEvent(AlertLevel.CRITICAL, "msg", "comp")))
                     .isEqualTo("#alerts-critical");

             assertThat(slackListener.getChannelFor(
                     new SystemAlertEvent(AlertLevel.WARNING, "msg", "comp")))
                     .isEqualTo("#alerts");
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 6: Tests para DashboardUpdateListener
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("4.6 - DashboardUpdateListener")
     class DashboardUpdateListenerTests {

         private DashboardUpdateListener dashboardListener;

         @BeforeEach
         void setUp() {
             dashboardListener = new DashboardUpdateListener();
         }

         @Test
         @DisplayName("Debe actualizar métricas para UserRegisteredEvent")
         void shouldUpdateMetricsForUserRegistered() {
             UserRegisteredEvent event = new UserRegisteredEvent("a@b.com", "Test");

             dashboardListener.onEvent(event);

             // Verificar que la métrica se actualizó
             assertThat(dashboardListener.getMetric("new_users")).isGreaterThan(0);
         }

         @Test
         @DisplayName("Debe actualizar métricas para OrderPlacedEvent")
         void shouldUpdateMetricsForOrderPlaced() {
             OrderPlacedEvent event = new OrderPlacedEvent("O1", "a@b.com", null, 150.0);

             dashboardListener.onEvent(event);

             assertThat(dashboardListener.getMetric("orders_today")).isGreaterThan(0);
         }

         @Test
         @DisplayName("Debe soportar eventos de negocio")
         void shouldSupportBusinessEvents() {
             assertThat(dashboardListener.supports(UserRegisteredEvent.class)).isTrue();
             assertThat(dashboardListener.supports(OrderPlacedEvent.class)).isTrue();
             assertThat(dashboardListener.supports(PaymentReceivedEvent.class)).isTrue();
             assertThat(dashboardListener.supports(SystemAlertEvent.class)).isTrue();
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 7: Tests para AuditLogListener
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("4.7 - AuditLogListener")
     class AuditLogListenerTests {

         private AuditLogListener auditListener;

         @BeforeEach
         void setUp() {
             auditListener = new AuditLogListener();
         }

         @Test
         @DisplayName("Debe loggear todos los eventos")
         void shouldLogAllEvents() {
             Event event1 = new UserRegisteredEvent("a@b.com", "Test");
             Event event2 = new OrderPlacedEvent("O1", "a@b.com", null, 100);

             auditListener.onEvent(event1);
             auditListener.onEvent(event2);

             List<AuditLogEntry> logs = auditListener.getLogs();
             assertThat(logs).hasSize(2);
         }

         @Test
         @DisplayName("Debe guardar timestamp en cada log")
         void shouldSaveTimestampInEachLog() {
             Event event = new UserRegisteredEvent("a@b.com", "Test");

             auditListener.onEvent(event);

             AuditLogEntry log = auditListener.getLogs().get(0);
             assertThat(log.getTimestamp()).isNotNull();
             assertThat(log.getEventType()).isEqualTo("USER_REGISTERED");
         }

         @Test
         @DisplayName("Debe soportar TODOS los tipos de eventos")
         void shouldSupportAllEventTypes() {
             assertThat(auditListener.supports(UserRegisteredEvent.class)).isTrue();
             assertThat(auditListener.supports(OrderPlacedEvent.class)).isTrue();
             assertThat(auditListener.supports(PaymentReceivedEvent.class)).isTrue();
             assertThat(auditListener.supports(SystemAlertEvent.class)).isTrue();
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 8: Tests para EventPublisher (Subject/Observable)
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("4.8 - EventPublisher (Subject)")
     class EventPublisherTests {

         private EventPublisher publisher;

         @BeforeEach
         void setUp() {
             publisher = new EventPublisher();
         }

         @Test
         @DisplayName("Debe permitir suscribir listeners")
         void shouldAllowSubscribingListeners() {
             EventListener listener = mock(EventListener.class);

             publisher.subscribe(listener);

             assertThat(publisher.getListenerCount()).isEqualTo(1);
         }

         @Test
         @DisplayName("Debe permitir desuscribir listeners")
         void shouldAllowUnsubscribingListeners() {
             EventListener listener = mock(EventListener.class);

             publisher.subscribe(listener);
             publisher.unsubscribe(listener);

             assertThat(publisher.getListenerCount()).isEqualTo(0);
         }

         @Test
         @DisplayName("Debe notificar a todos los listeners suscritos")
         void shouldNotifyAllSubscribedListeners() {
             EventListener listener1 = mock(EventListener.class);
             EventListener listener2 = mock(EventListener.class);
             when(listener1.supports(any())).thenReturn(true);
             when(listener2.supports(any())).thenReturn(true);

             publisher.subscribe(listener1);
             publisher.subscribe(listener2);

             Event event = new UserRegisteredEvent("a@b.com", "Test");
             publisher.publish(event);

             verify(listener1).onEvent(event);
             verify(listener2).onEvent(event);
         }

         @Test
         @DisplayName("Solo debe notificar a listeners que soportan el evento")
         void shouldOnlyNotifyListenersThatSupportEvent() {
             EventListener supportsEvent = mock(EventListener.class);
             EventListener doesNotSupport = mock(EventListener.class);

             when(supportsEvent.supports(UserRegisteredEvent.class)).thenReturn(true);
             when(doesNotSupport.supports(UserRegisteredEvent.class)).thenReturn(false);

             publisher.subscribe(supportsEvent);
             publisher.subscribe(doesNotSupport);

             Event event = new UserRegisteredEvent("a@b.com", "Test");
             publisher.publish(event);

             verify(supportsEvent).onEvent(event);
             verify(doesNotSupport, never()).onEvent(any());
         }
     }

    // // ═══════════════════════════════════════════════════════════════════════════
    // // PARTE 9: Tests para EventManager (Facade)
    // // ═══════════════════════════════════════════════════════════════════════════

     @Nested
     @DisplayName("4.9 - EventManager (Facade)")
     class EventManagerTests {

         @Mock
         private EventPublisher mockPublisher;

         private EventManager eventManager;

         @BeforeEach
         void setUp() {
             eventManager = new EventManager(mockPublisher);
         }

         @Test
         @DisplayName("Debe publicar eventos a través del publisher")
         void shouldPublishEventsThroughPublisher() {
             Event event = new UserRegisteredEvent("a@b.com", "Test");

             eventManager.emit(event);

             verify(mockPublisher).publish(event);
         }

         @Test
         @DisplayName("Debe permitir registrar listeners")
         void shouldAllowRegisteringListeners() {
             EventListener listener = mock(EventListener.class);

             eventManager.registerListener(listener);

             verify(mockPublisher).subscribe(listener);
         }

         @Test
         @DisplayName("Debe permitir remover listeners")
         void shouldAllowRemovingListeners() {
             EventListener listener = mock(EventListener.class);

             eventManager.removeListener(listener);

             verify(mockPublisher).unsubscribe(listener);
         }
     }
}
