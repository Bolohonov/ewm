package dev.bolohonov.server.model;

public enum EventState {
    /**
     * Ожидает утверждения
     */
    PENDING,
    /**
     * Опубликовано
     */
    PUBLISHED,
    /**
     * В архиве
     */
    CANCELED
}
