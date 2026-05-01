package exercise2_orders.refactored.repository;

import exercise2_orders.refactored.model.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<String, Order> ordersDatabase;

    public InMemoryOrderRepository() {
        this.ordersDatabase = new HashMap<>();
    }


    @Override
    public void save(Order order) {
        ordersDatabase.put(order.getOrderId(), order);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(ordersDatabase.get(orderId));
    }

    @Override
    public List<Order> findAll() {
        return ordersDatabase.values().stream().toList();
    }

    @Override
    public void delete(String orderId) {
        ordersDatabase.remove(orderId);
    }
}
