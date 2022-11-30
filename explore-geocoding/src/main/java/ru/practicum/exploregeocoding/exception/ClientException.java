package ru.practicum.exploregeocoding.exception;

/**
 * Ошибка клиента
 */
public class ClientException extends RuntimeException {
    public ClientException(final String message, final Exception cause) {
        super(message, cause);
    }
}
