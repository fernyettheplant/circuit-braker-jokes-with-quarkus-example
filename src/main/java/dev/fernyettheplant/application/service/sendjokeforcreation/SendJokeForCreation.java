package dev.fernyettheplant.application.service.sendjokeforcreation;

import dev.fernyettheplant.application.service.CommandHandler;
import dev.fernyettheplant.application.service.sendjokeforcreation.command.SendJokeForCreationCommand;
import dev.fernyettheplant.domain.model.Joke;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SendJokeForCreation implements CommandHandler<SendJokeForCreationCommand, Void> {
    private static final Logger log = Logger.getLogger(SendJokeForCreation.class);

    @Override
    public Void handle(SendJokeForCreationCommand command) {
        Joke joke = Joke.create(command.jokeText());

        return null;
    }
}
