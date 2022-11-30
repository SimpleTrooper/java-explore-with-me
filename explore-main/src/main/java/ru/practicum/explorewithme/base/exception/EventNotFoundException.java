package ru.practicum.explorewithme.base.exception;

/**
 * Событие не найдено
 */
public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(final String message) {
        super(message);
    }
}
