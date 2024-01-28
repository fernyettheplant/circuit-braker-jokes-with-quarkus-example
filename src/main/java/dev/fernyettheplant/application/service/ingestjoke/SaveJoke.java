package dev.fernyettheplant.application.service.ingestjoke;

import dev.fernyettheplant.application.service.CommandHandler;
import dev.fernyettheplant.application.service.ingestjoke.command.SaveJokeCommand;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SaveJoke implements CommandHandler<SaveJokeCommand, Void> {

    private final Logger logger;

    public SaveJoke(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Void handle(SaveJokeCommand command) {
        logger.info("Receivec Command " + command.toString());
        return null;
    }
}
