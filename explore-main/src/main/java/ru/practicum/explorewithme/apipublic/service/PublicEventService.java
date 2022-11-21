package ru.practicum.explorewithme.apipublic.service;

import ru.practicum.explorewithme.apipublic.controller.request.PublicGetEventsRequest;
import ru.practicum.explorewithme.apipublic.dto.PublicEventFullDto;
import ru.practicum.explorewithme.apipublic.dto.PublicEventShortDto;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Бизнес-логика событий публичного API
 */
public interface PublicEventService {
    /**
     * Получение событий с возможностью фильтрации
     * @param publicGetEventsRequest фильтрация
     * @param paginationRequest пагинация
     * @return Список коротких DTO событий
     */
    List<PublicEventShortDto> findAll(PublicGetEventsRequest publicGetEventsRequest,
                                      PaginationRequest paginationRequest);

    /**
     * Получение события по id
     * @param eventId id события
     * @return Полный DTO события
     */
    PublicEventFullDto findById(Long eventId);
}
