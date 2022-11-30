package ru.practicum.explorewithme.base.exception;

/**
 * Локация не найдена
 */
public class LocationNotFoundException extends NotFoundException {
    public LocationNotFoundException(final String message) {
        super(message);
    }
}
