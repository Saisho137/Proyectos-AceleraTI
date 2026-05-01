package exercise1_notifications.legacy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    
    // Almacén de logs (debería estar en otra clase - viola SRP)
    private List<String> notificationLogs = new ArrayList<>();
    
    /**
     * Método principal que envía notificaciones.
     *
     * PROBLEMAS:
     * - Switch/case gigante que viola OCP
     * - Múltiples responsabilidades mezcladas
     * - Difícil de testear unitariamente
     * - No permite mockear las dependencias
     */
    public boolean sendNotification(String type, String recipient, String message) {
        // Validación mezclada aquí (viola SRP)
        if (recipient == null || recipient.trim().isEmpty()) {
            logNotification("ERROR", type, recipient, "Recipient is empty");
            return false;
        }
        
        if (message == null || message.trim().isEmpty()) {
            logNotification("ERROR", type, recipient, "Message is empty");
            return false;
        }
        
        // Switch gigante - cada nuevo tipo requiere modificar esta clase (viola OCP)
        boolean success = false;
        switch (type.toUpperCase()) {
            case "EMAIL":
                success = sendEmail(recipient, message);
                break;
            case "SMS":
                success = sendSms(recipient, message);
                break;
            case "PUSH":
                success = sendPushNotification(recipient, message);
                break;
            default:
                logNotification("ERROR", type, recipient, "Unknown notification type: " + type);
                return false;
        }
        
        if (success) {
            logNotification("SUCCESS", type, recipient, message);
        } else {
            logNotification("FAILED", type, recipient, message);
        }
        
        return success;
    }
    
    /**
     * Envío de email - hardcodeado aquí (viola SRP y DIP)
     * Debería estar en su propia clase que implemente una interfaz.
     */
    private boolean sendEmail(String email, String message) {
        // Validación específica de email mezclada aquí
        if (!email.contains("@")) {
            return false;
        }
        
        // Formateo de mensaje mezclado aquí (viola SRP)
        String formattedMessage = formatEmailMessage(message);
        
        // Simulación de envío
        System.out.println(" Sending EMAIL to " + email + ": " + formattedMessage);
        return true;
    }
    
    /**
     * Envío de SMS - hardcodeado aquí (viola SRP y DIP)
     */
    private boolean sendSms(String phoneNumber, String message) {
        // Validación específica de teléfono mezclada aquí
        if (!phoneNumber.matches("\\+?\\d{10,15}")) {
            return false;
        }
        
        // Formateo de mensaje mezclado aquí (viola SRP)
        String formattedMessage = formatSmsMessage(message);
        
        // Simulación de envío
        System.out.println("📱 Sending SMS to " + phoneNumber + ": " + formattedMessage);
        return true;
    }
    
    /**
     * Envío de Push - hardcodeado aquí (viola SRP y DIP)
     */
    private boolean sendPushNotification(String deviceToken, String message) {
        // Validación específica de token mezclada aquí
        if (deviceToken.length() < 10) {
            return false;
        }
        
        // Formateo de mensaje mezclado aquí (viola SRP)
        String formattedMessage = formatPushMessage(message);
        
        // Simulación de envío
        System.out.println(" Sending PUSH to device " + deviceToken + ": " + formattedMessage);
        return true;
    }
    
    // ========== MÉTODOS DE FORMATEO (deberían estar separados - viola SRP) ==========
    
    private String formatEmailMessage(String message) {
        return "<html><body><h1>Notification</h1><p>" + message + "</p></body></html>";
    }
    
    private String formatSmsMessage(String message) {
        // SMS tiene límite de 160 caracteres
        if (message.length() > 160) {
            return message.substring(0, 157) + "...";
        }
        return message;
    }
    
    private String formatPushMessage(String message) {
        // Push tiene límite de 100 caracteres para el preview
        if (message.length() > 100) {
            return message.substring(0, 97) + "...";
        }
        return message;
    }
    
    // ========== LOGGING (debería estar separado - viola SRP) ==========
    
    private void logNotification(String status, String type, String recipient, String message) {
        String log = String.format("[%s] %s - Type: %s, To: %s, Message: %s",
                LocalDateTime.now(), status, type, recipient, message);
        notificationLogs.add(log);
        System.out.println(log);
    }
    
    public List<String> getNotificationLogs() {
        return new ArrayList<>(notificationLogs);
    }
    
    public void clearLogs() {
        notificationLogs.clear();
    }
}
