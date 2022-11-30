package ru.practicum.explorewithme.admin.service;

import ru.practicum.explorewithme.admin.controller.request.AdminGetEventsRequest;
import ru.practicum.explorewithme.admin.dto.AdminEventFullDto;
import ru.practicum.explorewithme.admin.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Бизнес-логика событий - admin API
 */
public interface AdminEventService {
    /**
     * Получить события
     *
     * @param getEventsRequest запрос с фильтрацией событий
     * @return Список DTO полученных событий
     */
    List<AdminEventFullDto> findAll(AdminGetEventsRequest getEventsRequest, PaginationRequest paginationRequest);

    /**
     * Обновить событие
     *
     * @param eventId                 id события
     * @param adminUpdateEventRequest параметры для обновления
     * @return DTO обновленного события
     */
    AdminEventFullDto update(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    /**
     * Опубликовать событие
     *
     * @param eventId id события
     * @return DTO опубликованного события
     */
    AdminEventFullDto publish(Long eventId);

    /**
     * Отклонить событие
     *
     * @param eventId id события
     * @return DTO отклоненного события
     */
    AdminEventFullDto reject(Long eventId);
}
