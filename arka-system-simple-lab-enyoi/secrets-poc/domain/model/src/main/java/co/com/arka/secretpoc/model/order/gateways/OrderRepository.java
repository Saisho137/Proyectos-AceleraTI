package co.com.arka.secretpoc.model.order.gateways;

import co.com.arka.secretpoc.model.order.Order;
import reactor.core.publisher.Mono;

public interface OrderRepository {
    Mono<Order> save(Order order);
    Mono<Order> findById(String id);
}
