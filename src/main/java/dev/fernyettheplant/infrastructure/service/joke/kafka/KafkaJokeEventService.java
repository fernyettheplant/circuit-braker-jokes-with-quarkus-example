package dev.fernyettheplant.infrastructure.service.joke.kafka;

import static java.time.ZoneOffset.UTC;

import dev.fernyettheplant.domain.model.Joke;
import dev.fernyettheplant.domain.service.joke.JokeEventService;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.v1.CloudEventBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

@Named("KafkaJokeEventService")
@ApplicationScoped
public class KafkaJokeEventService implements JokeEventService {
    private static final Logger log = Logger.getLogger(KafkaJokeEventService.class);

    private final Emitter<byte[]> cloudEventEmitter;

    public KafkaJokeEventService(@Channel("joke-ingested") Emitter<byte[]> cloudEventEmitter) {
        this.cloudEventEmitter = cloudEventEmitter;
    }

    @Override
    @CircuitBreaker(
            requestVolumeThreshold = 6,
            failureRatio = .5,
            delay = 10000 // 10 Secs
            )
    @Fallback(KafkaJokeEventCircuitBreakerFallback.class)
    public void sendForCreation(Joke joke) {
        CloudEvent cloudEvent = new CloudEventBuilder()
                .withId(joke.id().toString())
                .withTime(Instant.now().atOffset(UTC))
                .build();

        this.cloudEventEmitter.send(cloudEvent.toString().getBytes(StandardCharsets.UTF_8));
    }
}
