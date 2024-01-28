package dev.fernyettheplant.domain.service;

import dev.fernyettheplant.domain.model.Joke;

public interface JokeEventService {
    void sendForCreation(Joke joke);
}
