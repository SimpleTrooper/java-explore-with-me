package ru.practicum.explorewithme.base.exception;

/**
 * Сущность "пользователь" не найдена
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(final String message) {
        super(message);
    }
}
