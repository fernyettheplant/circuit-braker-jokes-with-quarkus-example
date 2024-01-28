package dev.fernyettheplant.application.service.sendjokeforcreation;

import dev.fernyettheplant.application.service.CommandHandler;
import dev.fernyettheplant.application.service.sendjokeforcreation.command.SendJokeForCreationCommand;
import dev.fernyettheplant.domain.model.Joke;
import dev.fernyettheplant.domain.service.joke.JokeEventService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SendJokeForCreation implements CommandHandler<SendJokeForCreationCommand, Void> {
    private static final Logger log = Logger.getLogger(SendJokeForCreation.class);

    private final JokeEventService jokeEventService;

    public SendJokeForCreation(@Named("KafkaJokeEventService") JokeEventService jokeEventService) {
        this.jokeEventService = jokeEventService;
    }

    @Override
    public Void handle(SendJokeForCreationCommand command) {
        Joke joke = Joke.create(command.jokeText());

        jokeEventService.sendForCreation(joke);
        return null;
    }
}
