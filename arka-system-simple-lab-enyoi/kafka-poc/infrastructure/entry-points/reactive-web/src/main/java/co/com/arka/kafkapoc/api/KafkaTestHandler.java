package co.com.arka.kafkapoc.api;

import co.com.arka.kafkapoc.usecase.publishevent.PublishEventUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaTestHandler {
    private final PublishEventUseCase publishEventUseCase;

    public Mono<ServerResponse> publishMessage(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PublishRequest.class)
                .flatMap(req -> publishEventUseCase.publish(req.message()))
                .flatMap(event -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(event));
    }

    public record PublishRequest(String message) {}
}
