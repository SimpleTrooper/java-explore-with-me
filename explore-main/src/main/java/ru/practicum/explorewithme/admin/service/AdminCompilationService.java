package ru.practicum.explorewithme.admin.service;

import ru.practicum.explorewithme.admin.dto.AdminCompilationDto;
import ru.practicum.explorewithme.admin.dto.NewCompilationDto;

/**
 * Бизнес-логика подборок событий API администратора
 */
public interface AdminCompilationService {
    /**
     * Добавление новой подборки
     * @param newCompilationDto DTO новой подборки
     * @return DTO с информацией о новой подборке
     */
    AdminCompilationDto add(NewCompilationDto newCompilationDto);

    /**
     * Удаление подборки
     * @param compilationId id удаляемой подборки
     */
    void delete(Long compilationId);

    /**
     * Удаление события из подборки
     * @param compilationId id подборки
     * @param eventId id события
     */
    void deleteEventFromCompilation(Long compilationId, Long eventId);

    /**
     * Добавление события в подборку
     * @param compilationId id подборки
     * @param eventId id события
     */
    void addEventToCompilation(Long compilationId, Long eventId);

    /**
     * Открепление подборки
     * @param compilationId id подборки
     */
    void unpinCompilation(Long compilationId);

    /**
     * Прикрепление подборки
     * @param compilationId id подборки
     */
    void pinCompilation(Long compilationId);
}

