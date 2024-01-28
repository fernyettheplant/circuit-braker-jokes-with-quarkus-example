package dev.fernyettheplant.infrastructure.service.joke.kafka;

import dev.fernyettheplant.domain.model.Joke;
import dev.fernyettheplant.domain.service.joke.JokeEventService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

@ApplicationScoped
public class KafkaJokeEventCircuitBreakerFallback implements FallbackHandler<Void> {
    private final JokeEventService redisJokeEventService;

    public KafkaJokeEventCircuitBreakerFallback(
            @Named("RedisJokeEventService") JokeEventService redisJokeEventService) {
        this.redisJokeEventService = redisJokeEventService;
    }

    @Override
    public Void handle(ExecutionContext executionContext) {
        Joke joke = (Joke) executionContext.getParameters()[0];
        redisJokeEventService.sendForCreation(joke);
        return null;
    }
}
