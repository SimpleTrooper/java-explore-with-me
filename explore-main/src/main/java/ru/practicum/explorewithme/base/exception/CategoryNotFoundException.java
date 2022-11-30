package ru.practicum.explorewithme.base.exception;

/**
 * Категория не найдена
 */
public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(final String message) {
        super(message);
    }
}
