package ru.practicum.explorewithme.apipublic.service;

import ru.practicum.explorewithme.apipublic.dto.PublicCompilationDto;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Бизнес-логика подборок событий публичного API
 */
public interface PublicCompilationService {
    /**
     * Получение подборок событий
     *
     * @param pinned            получить только закрепленные/не закрепленные подборки
     * @param paginationRequest запрос на пагинацию
     * @return Список DTO подборок
     */
    List<PublicCompilationDto> findAll(Boolean pinned, PaginationRequest paginationRequest);

    /**
     * Получение подборки по id
     *
     * @param compilationId id подборки
     * @return DTO полученной подборки
     */
    PublicCompilationDto findById(Long compilationId);
}
