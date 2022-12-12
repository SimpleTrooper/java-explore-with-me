package ru.practicum.explorewithme.apipublic.service;

import ru.practicum.explorewithme.apipublic.dto.PublicLocationDto;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Бизнес-логика локаций публичного API
 */
public interface PublicLocationService {
    /**
     * Получение всех событий с пагинацией
     *
     * @param paginationRequest пагинация
     * @return список DTO локаций
     */
    List<PublicLocationDto> findAll(PaginationRequest paginationRequest);

    /**
     * Получение локации по id
     *
     * @param locationId id локации
     * @return DTO локации
     */
    PublicLocationDto findById(Long locationId);
}
