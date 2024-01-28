package dev.fernyettheplant.infrastructure.service.joke.redis;

import dev.fernyettheplant.domain.model.Joke;
import dev.fernyettheplant.domain.service.joke.JokeEventService;
import dev.fernyettheplant.infrastructure.service.joke.kafka.KafkaJokeEventService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.jboss.logging.Logger;

@Named("RedisJokeEventService")
@ApplicationScoped
public class RedisJokeEventService implements JokeEventService {
    private static final Logger log = Logger.getLogger(RedisJokeEventService.class);

    @Override
    public void sendForCreation(Joke joke) {
        log.info("in redis");
    }
}
