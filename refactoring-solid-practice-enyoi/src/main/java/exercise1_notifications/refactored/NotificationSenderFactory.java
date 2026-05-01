package exercise1_notifications.refactored;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NotificationSenderFactory {
    /*
     El final funciona como el const en JS,
     una vez inicializado no puede cambiar la referencia del objeto,
     pero sí su contenido.

      Además, con esto se asegura que solo se pueda modificar el HashMap
      a través de los métodos definidos en esta clase,
      evitando modificaciones externas no controladas.
     */
    private final Map<String, Supplier<NotificationSender>> registers;

    public NotificationSenderFactory() {
        this.registers = new HashMap<>();

        registers.put("EMAIL", EmailNotificationSender::new);
        registers.put("SMS", SmsNotificationSender::new);
        registers.put("PUSH", PushNotificationSender::new);
    }

    public NotificationSender createSender(String type) {
        Supplier<NotificationSender> supplier = registers.get(type.toUpperCase());
        if (supplier == null)
            throw new IllegalArgumentException("Unknown notification type: " + type);

        NotificationSender notificationSender = supplier.get();
        if (notificationSender == null)
            throw new IllegalStateException("Notification sender for type " + type + " could not be created.");

        return notificationSender;
    }

    public void registerSender(String type, Supplier<NotificationSender> supplier) {
        this.registers.put(type.toUpperCase(), supplier);
    }
}
