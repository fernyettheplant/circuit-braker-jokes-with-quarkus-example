package dev.fernyettheplant.infrastructure.service.joke.kafka;

import dev.fernyettheplant.domain.model.Joke;
import dev.fernyettheplant.domain.service.joke.JokeEventService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.jboss.logging.Logger;

@Named("KafkaJokeEventService")
@ApplicationScoped
public class KafkaJokeEventService implements JokeEventService {
    private static final Logger log = Logger.getLogger(KafkaJokeEventService.class);

    @Override
    @CircuitBreaker(
            requestVolumeThreshold = 6,
            failureRatio = .5,
            delay = 10000 // 10 Secs
            )
    @Fallback(KafkaJokeEventCircuitBreakerFallback.class)
    public void sendForCreation(Joke joke) {}
}
