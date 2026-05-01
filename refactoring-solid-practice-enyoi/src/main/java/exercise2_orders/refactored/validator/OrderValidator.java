package exercise2_orders.refactored.validator;

import exercise2_orders.refactored.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderValidator {
    public ValidationResult validate(List<OrderItem> items) {

        if (items == null || items.isEmpty()) {
            return ValidationResult.invalid(List.of("Order must have at least one item"));
        }

        List<String> errors = new ArrayList<>();
        boolean sw = false;

        for (OrderItem item : items) {
            if (item.getName().isEmpty()) {
                errors.add("Name está vacío");
                sw = true;
            }

            double price = item.getPrice().doubleValue();
            int quantity = item.getQuantity();

            if (price <= 0 || quantity <= 0) {
                errors.add("Price and Quantity must be positive");
                sw = true;
            }

        }

        if (!sw) {
            return ValidationResult.valid();
        } else {
            return ValidationResult.invalid(errors);
        }
    }
}
