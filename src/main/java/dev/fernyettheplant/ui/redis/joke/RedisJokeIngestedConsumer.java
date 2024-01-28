package dev.fernyettheplant.ui.redis.joke;

import static java.util.Objects.nonNull;

import dev.fernyettheplant.application.service.CommandHandler;
import dev.fernyettheplant.application.service.ingestjoke.command.SaveJokeCommand;
import dev.fernyettheplant.ui.events.EventUtils;
import dev.fernyettheplant.ui.events.model.JokeIngestedEventData;
import io.cloudevents.CloudEvent;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.KeyValue;
import io.quarkus.redis.datasource.list.ListCommands;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.time.Duration;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RedisJokeIngestedConsumer implements Runnable {
    private final Logger logger;
    private final CommandHandler<SaveJokeCommand, Void> saveJoke;
    private final ListCommands<String, byte[]> queue;
    private volatile boolean stopped = false;

    public RedisJokeIngestedConsumer(
            CommandHandler<SaveJokeCommand, Void> saveJoke, Logger logger, RedisDataSource redisDataSource) {
        this.logger = logger;
        this.saveJoke = saveJoke;
        this.queue = redisDataSource.list(byte[].class);
    }

    public void start(@Observes StartupEvent ev) {
        new Thread(this).start();
    }

    public void stop(@Observes ShutdownEvent ev) {
        stopped = true;
    }

    @Override
    public void run() {
        logger.info("Starting listening Redis Queue");

        while (!stopped) {
            KeyValue<String, byte[]> item = this.queue.brpop(Duration.ofSeconds(1), "create-jokes-queue");
            if (nonNull(item)) {
                logger.info("Got Item");

                CloudEvent cloudEvent = EventUtils.convertToCloudEvent(item.value());
                logger.info(cloudEvent.getType());

                JokeIngestedEventData event = EventUtils.getDataFromCloudEvent(cloudEvent, JokeIngestedEventData.class);
                logger.info(event);

                saveJoke.handle(new SaveJokeCommand(event.id(), event.text()));
            }
        }

        logger.info("Stopped");
    }
}
