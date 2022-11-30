package ru.practicum.explorewithme.apiprivate.service;

import ru.practicum.explorewithme.apiprivate.dto.NewEventDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventFullDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventShortDto;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Бизнес-логика событий закрытого API
 */
public interface PrivateEventService {
    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param initiatorId       id пользователя
     * @param paginationRequest пагинация
     * @return список коротких DTO событий
     */
    List<PrivateEventShortDto> findAllByInitiatorId(Long initiatorId, PaginationRequest paginationRequest);

    /**
     * Обновить событие
     *
     * @param userId      id пользователя-инициатора
     * @param newEventDto DTO обновления
     * @return полное DTO обновленного события
     */
    PrivateEventFullDto update(Long userId, NewEventDto newEventDto);

    /**
     * Добавить событие
     *
     * @param userId      id пользователя-инициатора
     * @param newEventDto DTO нового события
     * @return полное DTO нового события
     */
    PrivateEventFullDto add(Long userId, NewEventDto newEventDto);

    /**
     * Получение полной информации о событии, добавленном текущим пользователем
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return Полное DTO события
     */
    PrivateEventFullDto findById(Long userId, Long eventId);

    /**
     * Отмена события, добавленного текущим пользователем
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return Полное DTO события
     */
    PrivateEventFullDto cancel(Long userId, Long eventId);
}
