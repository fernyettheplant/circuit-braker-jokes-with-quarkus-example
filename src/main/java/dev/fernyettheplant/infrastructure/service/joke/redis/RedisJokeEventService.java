package dev.fernyettheplant.infrastructure.service.joke.redis;

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
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.ListCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.net.URI;
import java.time.Instant;
import org.jboss.logging.Logger;

@Named("RedisJokeEventService")
@ApplicationScoped
public class RedisJokeEventService implements JokeEventService {
    private static final Logger log = Logger.getLogger(RedisJokeEventService.class);

    private final ListCommands<String, byte[]> queue;
    private final ObjectMapper objectMapper;

    public RedisJokeEventService(RedisDataSource redisDataSource, ObjectMapper objectMapper) {
        this.queue = redisDataSource.list(byte[].class);
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendForCreation(Joke joke) {
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

        queue.lpush("create-jokes-queue", serialized);
        log.info("Pushed to Redis Queue");
    }
}
