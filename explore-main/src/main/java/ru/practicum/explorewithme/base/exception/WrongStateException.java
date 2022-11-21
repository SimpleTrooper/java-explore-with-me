package ru.practicum.explorewithme.base.exception;

/**
 * Состояние события не найдено
 */
public class WrongStateException extends RuntimeException {
    public WrongStateException(final String message) {
        super(message);
    }
}
