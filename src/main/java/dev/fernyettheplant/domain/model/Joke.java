package dev.fernyettheplant.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class Joke {
    private final UUID id;
    private final String text;

    private Joke(UUID id, String text) {
        this.id = id;
        this.text = text;
    }

    public static Joke create(String text) {
        return new Joke(UUID.randomUUID(), text);
    }

    public static Joke create(String id, String text) {
        return new Joke(UUID.fromString(id), text);
    }

    public UUID id() {
        return id;
    }

    public String text() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joke joke = (Joke) o;
        return Objects.equals(id, joke.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
