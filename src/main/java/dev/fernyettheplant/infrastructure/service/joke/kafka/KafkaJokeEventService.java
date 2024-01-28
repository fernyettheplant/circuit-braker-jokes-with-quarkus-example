package dev.fernyettheplant.infrastructure.service.joke.kafka;

import static java.time.ZoneOffset.UTC;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fernyettheplant.domain.model.Joke;
import dev.fernyettheplant.domain.service.joke.JokeEventService;
import dev.fernyettheplant.infrastructure.service.joke.events.JokeIngested;
import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventData;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.net.URI;
import java.time.Instant;
import java.util.Random;
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

    private final ObjectMapper objectMapper;

    public KafkaJokeEventService(
            @Channel("joke-ingested-out") Emitter<byte[]> cloudEventEmitter, ObjectMapper objectMapper) {
        this.cloudEventEmitter = cloudEventEmitter;
        this.objectMapper = objectMapper;
    }

    @Override
    @CircuitBreaker(
            requestVolumeThreshold = 6, // Latest 6
            failureRatio = .5, // 50% Failure Rate
            delay = 10000 // 10 Secs
            )
    @Fallback(KafkaJokeEventCircuitBreakerFallback.class)
    public void sendForCreation(Joke joke) {
//        Random random = new Random();
//
//        if (random.nextBoolean()) {
//             Simulating a failure
//            throw new RuntimeException("Failure occurred");
//        }

        try {
            JokeIngested jokeIngestedEvent = new JokeIngested(joke.id().toString(), joke.text());
            CloudEventData eventData = PojoCloudEventData.wrap(jokeIngestedEvent, objectMapper::writeValueAsBytes);

            CloudEvent cloudEvent = CloudEventBuilder.v1()
                    .withId(joke.id().toString())
                    .withTime(Instant.now().atOffset(UTC))
                    .withSource(URI.create("fernyettheplant.dev"))
                    .withType("dev.fernyettheplant.joke.ingested")
                    .withSubject("joke")
                    .withData("application/json", eventData)
                    .build();

            EventFormat format = EventFormatProvider.getInstance().resolveFormat(JsonFormat.CONTENT_TYPE);

            byte[] serialized = format.serialize(cloudEvent);
            this.cloudEventEmitter.send(serialized);
        } catch (Exception e) {
            log.error("An error occurred sending event in Kafka", e);
            throw new RuntimeException(e);
        }
    }
}
