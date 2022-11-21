package ru.practicum.explorewithme.base.exception;

/**
 * Сущность не найдена
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}
