package exercise2_orders.refactored.model;

import java.math.BigDecimal;

public class OrderItem {
    private final String name;
    private final BigDecimal price;
    private final int quantity;

    public OrderItem(String name, BigDecimal price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
