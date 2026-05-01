package exercise2_orders.refactored.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private final String id;
    private final Customer customer;
    private final List<OrderItem> items;
    private OrderStatus status;
    private BigDecimal total;

    public Order(String id, Customer customer, List<OrderItem> items) {
        this.id = id;
        this.customer = customer;
        this.items = items;
        this.status = OrderStatus.CREATED;
    }


    public String getOrderId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getSubtotal() {
        return items.stream()
                .map(OrderItem::getTotal) // (orderItem -> orderItem.getTotal()
                .reduce(BigDecimal.ZERO, BigDecimal::add); // (total1, total2) -> total1.add(total2)
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
