package exercise4_events.legacy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    
    // Logs de eventos (debería estar separado)
    private List<Map<String, Object>> eventLogs = new ArrayList<>();
    
    // Configuración hardcodeada (viola DIP)
    private String adminEmail = "admin@company.com";
    private String adminPhone = "+1234567890";
    private String slackWebhook = "https://hooks.slack.com/services/xxx";
    
    /**
     * Maneja un evento y realiza todas las notificaciones necesarias.
     * 
     * PROBLEMAS:
     * - Switch gigante para cada tipo de evento
     * - Cada rama hace múltiples cosas
     * - Imposible agregar nuevos listeners sin modificar
     * - Acoplamiento directo a métodos de notificación
     */
    public void handleEvent(String eventType, Map<String, Object> eventData) {
        // Log del evento (debería estar separado)
        logEvent(eventType, eventData);
        
        // Switch gigante - cada tipo de evento tiene lógica diferente
        switch (eventType.toUpperCase()) {
            case "USER_REGISTERED":
                handleUserRegistered(eventData);
                break;
            case "ORDER_PLACED":
                handleOrderPlaced(eventData);
                break;
            case "PAYMENT_RECEIVED":
                handlePaymentReceived(eventData);
                break;
            case "SYSTEM_ALERT":
                handleSystemAlert(eventData);
                break;
            default:
                System.out.println("⚠️ Unknown event type: " + eventType);
        }
    }
    
    /**
     * Manejo de registro de usuario.
     * Mezcla múltiples responsabilidades.
     */
    private void handleUserRegistered(Map<String, Object> data) {
        String userEmail = (String) data.get("email");
        String userName = (String) data.get("name");
        
        // 1. Enviar email de bienvenida
        sendWelcomeEmail(userEmail, userName);
        
        // 2. Notificar al equipo de marketing
        sendEmailToMarketing(userName, userEmail);
        
        // 3. Actualizar dashboard de nuevos usuarios
        updateDashboard("new_users", 1);
        
        // 4. Notificar en Slack
        sendSlackNotification("#new-users", "New user registered: " + userName);
    }
    
    /**
     * Manejo de pedido realizado.
     * Múltiples notificaciones hardcodeadas.
     */
    private void handleOrderPlaced(Map<String, Object> data) {
        String orderId = (String) data.get("orderId");
        String customerEmail = (String) data.get("customerEmail");
        Double amount = (Double) data.get("amount");
        
        // 1. Enviar confirmación por email al cliente
        sendOrderConfirmationEmail(customerEmail, orderId, amount);
        
        // 2. Enviar SMS al cliente
        String customerPhone = (String) data.get("customerPhone");
        if (customerPhone != null) {
            sendOrderSms(customerPhone, orderId);
        }
        
        // 3. Notificar al equipo de fulfillment
        sendSlackNotification("#orders", "New order: " + orderId + " - $" + amount);
        
        // 4. Actualizar dashboard de ventas
        updateDashboard("orders_today", 1);
        updateDashboard("revenue_today", amount.intValue());
    }
    
    /**
     * Manejo de pago recibido.
     */
    private void handlePaymentReceived(Map<String, Object> data) {
        String paymentId = (String) data.get("paymentId");
        String orderId = (String) data.get("orderId");
        Double amount = (Double) data.get("amount");
        String customerEmail = (String) data.get("customerEmail");
        
        // 1. Enviar recibo por email
        sendPaymentReceiptEmail(customerEmail, paymentId, amount);
        
        // 2. Notificar a contabilidad
        sendSlackNotification("#accounting", "Payment received: " + paymentId + " - $" + amount);
        
        // 3. Actualizar dashboard financiero
        updateDashboard("payments_today", 1);
        updateDashboard("total_received", amount.intValue());
    }
    
    /**
     * Manejo de alerta del sistema.
     * Notificaciones urgentes.
     */
    private void handleSystemAlert(Map<String, Object> data) {
        String alertLevel = (String) data.get("level"); // INFO, WARNING, CRITICAL
        String message = (String) data.get("message");
        String component = (String) data.get("component");
        
        // 1. Siempre log
        System.out.println("🚨 SYSTEM ALERT [" + alertLevel + "] " + component + ": " + message);
        
        // 2. Slack para todos los niveles
        String channel = alertLevel.equals("CRITICAL") ? "#alerts-critical" : "#alerts";
        sendSlackNotification(channel, "[" + alertLevel + "] " + component + ": " + message);
        
        // 3. Email solo para WARNING y CRITICAL
        if (alertLevel.equals("WARNING") || alertLevel.equals("CRITICAL")) {
            sendAlertEmail(adminEmail, alertLevel, component, message);
        }
        
        // 4. SMS solo para CRITICAL
        if (alertLevel.equals("CRITICAL")) {
            sendAlertSms(adminPhone, component, message);
        }
        
        // 5. Actualizar dashboard de salud del sistema
        updateDashboard("system_alerts_" + alertLevel.toLowerCase(), 1);
    }
    
    // ========== MÉTODOS DE NOTIFICACIÓN (cada uno debería ser su propia clase) ==========
    
    private void sendWelcomeEmail(String email, String name) {
        System.out.println(" Sending welcome email to " + email);
        System.out.println("   Subject: Welcome to our platform, " + name + "!");
    }
    
    private void sendEmailToMarketing(String userName, String userEmail) {
        System.out.println(" Notifying marketing about new user: " + userName);
    }
    
    private void sendOrderConfirmationEmail(String email, String orderId, Double amount) {
        System.out.println(" Sending order confirmation to " + email);
        System.out.println("   Order: " + orderId + " - $" + amount);
    }
    
    private void sendPaymentReceiptEmail(String email, String paymentId, Double amount) {
        System.out.println(" Sending payment receipt to " + email);
        System.out.println("   Payment: " + paymentId + " - $" + amount);
    }
    
    private void sendAlertEmail(String email, String level, String component, String message) {
        System.out.println(" ALERT EMAIL to " + email);
        System.out.println("   [" + level + "] " + component + ": " + message);
    }
    
    private void sendOrderSms(String phone, String orderId) {
        System.out.println(" SMS to " + phone + ": Your order " + orderId + " has been placed!");
    }
    
    private void sendAlertSms(String phone, String component, String message) {
        System.out.println(" CRITICAL ALERT SMS to " + phone);
        System.out.println("   " + component + ": " + message);
    }
    
    private void sendSlackNotification(String channel, String message) {
        System.out.println(" Slack [" + channel + "]: " + message);
    }
    
    private void updateDashboard(String metric, int value) {
        System.out.println(" Dashboard update: " + metric + " += " + value);
    }
    
    private void logEvent(String eventType, Map<String, Object> data) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("timestamp", LocalDateTime.now().toString());
        logEntry.put("eventType", eventType);
        logEntry.put("data", new HashMap<>(data));
        eventLogs.add(logEntry);
        System.out.println(" Event logged: " + eventType);
    }
    
    public List<Map<String, Object>> getEventLogs() {
        return new ArrayList<>(eventLogs);
    }
}
