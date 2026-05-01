package exercise2_orders.legacy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderProcessor {
    
    // "Base de datos" simulada - acoplamiento directo (viola DIP)
    private Map<String, Map<String, Object>> ordersDatabase = new HashMap<>();
    private int orderIdCounter = 1;
    
    /**
     * Procesa un pedido completo.
     * 
     * PROBLEMAS:
     * - Hace demasiadas cosas (viola SRP)
     * - Imposible de testear unitariamente
     * - Lógica de negocio mezclada con persistencia
     */
    public Map<String, Object> processOrder(String customerType, List<Map<String, Object>> items) {
        // ============ VALIDACIÓN (debería estar separada) ============
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        
        for (Map<String, Object> item : items) {
            if (!item.containsKey("name") || !item.containsKey("price") || !item.containsKey("quantity")) {
                throw new IllegalArgumentException("Each item must have name, price and quantity");
            }
            double price = ((Number) item.get("price")).doubleValue();
            int quantity = ((Number) item.get("quantity")).intValue();
            if (price <= 0 || quantity <= 0) {
                throw new IllegalArgumentException("Price and quantity must be positive");
            }
        }
        
        // ============ CÁLCULO DE SUBTOTAL ============
        double subtotal = 0;
        for (Map<String, Object> item : items) {
            double price = ((Number) item.get("price")).doubleValue();
            int quantity = ((Number) item.get("quantity")).intValue();
            subtotal += price * quantity;
        }
        
        // ============ CÁLCULO DE DESCUENTO (viola OCP - switch gigante) ============
        double discountPercentage = 0;
        switch (customerType.toUpperCase()) {
            case "REGULAR":
                discountPercentage = 0;
                break;
            case "PREMIUM":
                discountPercentage = 0.10; // 10%
                // Premium también tiene envío gratis si compra > $100
                break;
            case "VIP":
                discountPercentage = 0.20; // 20%
                // VIP tiene envío gratis siempre
                break;
            case "EMPLOYEE":
                discountPercentage = 0.30; // 30%
                break;
            default:
                discountPercentage = 0;
        }
        
        double discountAmount = subtotal * discountPercentage;
        double afterDiscount = subtotal - discountAmount;
        
        // ============ CÁLCULO DE IMPUESTOS (debería estar separado) ============
        double taxRate = 0.19; // 19% IVA
        double taxAmount = afterDiscount * taxRate;
        double total = afterDiscount + taxAmount;
        
        // ============ CÁLCULO DE ENVÍO (lógica compleja mezclada) ============
        double shippingCost = calculateShipping(customerType, subtotal);
        total += shippingCost;
        
        // ============ PERSISTENCIA (debería estar en Repository) ============
        String orderId = "ORD-" + String.format("%05d", orderIdCounter++);
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("customerType", customerType);
        order.put("items", new ArrayList<>(items));
        order.put("subtotal", subtotal);
        order.put("discountPercentage", discountPercentage);
        order.put("discountAmount", discountAmount);
        order.put("taxAmount", taxAmount);
        order.put("shippingCost", shippingCost);
        order.put("total", total);
        order.put("status", "CREATED");
        order.put("createdAt", LocalDateTime.now().toString());
        
        ordersDatabase.put(orderId, order);
        
        return order;
    }
    
    /**
     * Cálculo de envío con lógica compleja mezclada.
     * Viola SRP y OCP.
     */
    private double calculateShipping(String customerType, double subtotal) {
        // Lógica de envío hardcodeada
        if (customerType.equalsIgnoreCase("VIP")) {
            return 0; // VIP siempre gratis
        }
        if (customerType.equalsIgnoreCase("PREMIUM") && subtotal > 100) {
            return 0; // Premium gratis si compra > $100
        }
        if (subtotal > 200) {
            return 0; // Envío gratis para compras grandes
        }
        return 9.99; // Costo estándar
    }
    

    /**
     * Obtener una orden por ID.
     * Acceso directo a la "base de datos" (viola DIP).
     */
    public Map<String, Object> getOrder(String orderId) {
        return ordersDatabase.get(orderId);
    }
    
    /**
     * Obtener todas las órdenes (para testing).
     */
    public List<Map<String, Object>> getAllOrders() {
        return new ArrayList<>(ordersDatabase.values());
    }
}
