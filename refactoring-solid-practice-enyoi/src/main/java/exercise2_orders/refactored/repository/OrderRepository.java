package exercise2_orders.refactored.repository;

import exercise2_orders.refactored.model.Order;
import exercise2_orders.refactored.model.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(String orderId);
    List<Order> findAll();
    void delete(String orderId);
}
