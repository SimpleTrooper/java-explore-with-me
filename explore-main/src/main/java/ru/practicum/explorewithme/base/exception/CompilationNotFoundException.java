package ru.practicum.explorewithme.base.exception;

/**
 * Подборка событий не найдена
 */
public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(final String message) {
        super(message);
    }
}
