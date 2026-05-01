package co.com.arka.kafkapoc.model.events.gateways;

import co.com.arka.kafkapoc.model.events.TestEvent;
import reactor.core.publisher.Mono;

public interface EventsGateway {
    Mono<Void> emit(TestEvent event);
}
