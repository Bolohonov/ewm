package dev.bolohonov.model;

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
