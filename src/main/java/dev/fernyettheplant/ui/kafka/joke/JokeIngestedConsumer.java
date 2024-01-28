package dev.fernyettheplant.ui.kafka.joke;

import dev.fernyettheplant.application.service.CommandHandler;
import dev.fernyettheplant.application.service.ingestjoke.command.SaveJokeCommand;
import dev.fernyettheplant.ui.events.EventUtils;
import dev.fernyettheplant.ui.events.model.JokeIngestedEventData;
import io.cloudevents.CloudEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class JokeIngestedConsumer {
    private final Logger logger;
    private final CommandHandler<SaveJokeCommand, Void> saveJoke;

    public JokeIngestedConsumer(Logger logger, CommandHandler<SaveJokeCommand, Void> saveJoke) {
        this.logger = logger;
        this.saveJoke = saveJoke;
    }

    @Incoming("joke-ingested-in")
    public void consume(byte[] message) {
        logger.info("Message received from kafka");
        CloudEvent cloudEvent = EventUtils.convertToCloudEvent(message);
        JokeIngestedEventData event = EventUtils.getDataFromCloudEvent(cloudEvent, JokeIngestedEventData.class);
        saveJoke.handle(new SaveJokeCommand(event.id(), event.text()));
    }
}
