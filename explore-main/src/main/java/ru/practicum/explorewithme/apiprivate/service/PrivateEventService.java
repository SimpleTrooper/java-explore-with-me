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
    List<PrivateEventShortDto> findAllByInitiatorId(Long initiatorId, PaginationRequest paginationRequest);

    PrivateEventFullDto update(Long userId, NewEventDto newEventDto);

    PrivateEventFullDto add(Long userId, NewEventDto newEventDto);

    PrivateEventFullDto findById(Long userId, Long eventId);

    PrivateEventFullDto cancel(Long userId, Long eventId);
}
