package dev.fernyettheplant.domain.service.joke;

import dev.fernyettheplant.domain.model.Joke;

public interface JokeEventService {
    void sendForCreation(Joke joke);
}
