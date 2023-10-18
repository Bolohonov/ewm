package dev.bolohonov.model;

public enum RequestStatus {
    /**
     * Ожидает утверждения
     */
    PENDING,
    /**
     * Запрос подтвержден
     */
    CONFIRMED,
    /**
     * Заявка отклонена
     */
    REJECTED,
    /**
     * В архиве
     */
    CANCELED
}
