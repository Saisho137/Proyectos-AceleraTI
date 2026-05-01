package co.com.arka.secretpoc.api;

import co.com.arka.secretpoc.model.brokersecret.BrokerSecret;
import co.com.arka.secretpoc.model.order.Order;
import co.com.arka.secretpoc.model.order.gateways.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final BrokerSecret brokerSecret;
    private final OrderRepository orderRepository;

    public Mono<ServerResponse> secretsKafkaGet(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(brokerSecret);
    }

    public Mono<ServerResponse> saveOrder(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Order.class)
                .flatMap(orderRepository::save)
                .flatMap(savedOrder -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedOrder));
    }

    public Mono<ServerResponse> getOrderById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return orderRepository.findById(id)
                .flatMap(order -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(order))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
