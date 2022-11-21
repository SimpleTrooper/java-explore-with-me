package ru.practicum.explorewithme.base.exception;

/**
 * Требования к сущности не соблюдены
 */
public class ConditionsNotMetException extends RuntimeException {
    public ConditionsNotMetException(final String message) {
        super(message);
    }
}
