package ru.practicum.explorewithme.base.exception;

/**
 * Запрос на участие не найден
 */
public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(final String message) {
        super(message);
    }
}
