package dev.fernyettheplant.application.service;

public interface CommandHandler<C, R> {
    R handle(C command);
}
