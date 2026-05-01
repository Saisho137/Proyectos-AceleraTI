package co.com.arka.kafkapoc.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;

@Component
@Log4j2
@RequiredArgsConstructor
public class KafkaConsumer {
    private final KafkaReceiver<String, String> kafkaReceiver;

    @EventListener(ApplicationStartedEvent.class)
    public Flux<Object> listenMessages() {
        return kafkaReceiver
                .receive()
                .publishOn(Schedulers.newBoundedElastic(
                        Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE,
                        Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE,
                        "kafka"))
                .flatMap(receiverRecord -> {
                    log.info("EMA: Record received {}", receiverRecord.value());
                    receiverRecord.receiverOffset().acknowledge();
                    return Mono.empty();
                })
                .doOnError(error -> log.error("Error processing kafka record", error))
                .retry()
                .repeat();
    }
}
