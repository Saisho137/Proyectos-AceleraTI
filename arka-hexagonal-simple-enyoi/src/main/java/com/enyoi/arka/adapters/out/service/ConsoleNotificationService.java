package com.enyoi.arka.adapters.out.service;

import com.enyoi.arka.domain.ports.out.NotificationService;

public class ConsoleNotificationService implements NotificationService {

    @Override
    public void notifyOrderStatusChange(String orderId, String customerEmail, String newStatus) {
        System.out.println("Order status change - " +
                "Order ID: " + orderId + ", " +
                "Customer Email: " + customerEmail + ", " +
                "New Status: " + newStatus);
    }

    @Override
    public void notifyLowStockAlert(String productName, int currentStock) {
        System.out.println("Low stock Alert - " +
                "Product: " + productName + ", " +
                "Current Stock: " + currentStock + "stock bajo!");

    }
}
