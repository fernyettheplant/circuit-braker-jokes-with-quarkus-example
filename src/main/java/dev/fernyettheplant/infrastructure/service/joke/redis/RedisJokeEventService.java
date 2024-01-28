package dev.fernyettheplant.infrastructure.service.joke.redis;

import dev.fernyettheplant.domain.model.Joke;
import dev.fernyettheplant.domain.service.joke.JokeEventService;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.ListCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.jboss.logging.Logger;

@Named("RedisJokeEventService")
@ApplicationScoped
public class RedisJokeEventService implements JokeEventService {
    private static final Logger log = Logger.getLogger(RedisJokeEventService.class);

    private final ListCommands<String, String> commands;

    public RedisJokeEventService(RedisDataSource redisDataSource) {
        this.commands = redisDataSource.list(String.class);
    }

    @Override
    public void sendForCreation(Joke joke) {
        commands.lpush("create-jokes-queue", "test");
        log.info("Pushed to Redis Queue");
    }
}
