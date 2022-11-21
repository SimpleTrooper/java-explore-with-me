package ru.practicum.explorewithme.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.admin.controller.request.AdminGetEventsRequest;
import ru.practicum.explorewithme.admin.dto.AdminEventFullDto;
import ru.practicum.explorewithme.admin.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.admin.service.AdminEventService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер API администратора - события
 */
@Slf4j
@HandleExceptions
@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;

    @Autowired
    public AdminEventController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    /**
     * Получить события
     * @param users список id инициаторов
     * @param states список состояний
     * @param categories список id категорий
     * @param rangeStart дата начала - от
     * @param rangeEnd дата начала - до
     * @param from вывести записи начиная с
     * @param size количество записей
     * @return Список DTO полученных событий
     */
    @GetMapping
    public List<AdminEventFullDto> findAll(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<String> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) LocalDateTime rangeStart,
                                           @RequestParam(required = false) LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Admin request to find all events");
        List<EventState> eventStates = null;
        if (states != null) {
            eventStates = states.stream().map(EventState::from).collect(Collectors.toList());
        }
        AdminGetEventsRequest getEventsRequest = new AdminGetEventsRequest(users, eventStates, categories,
                rangeStart, rangeEnd);
        PaginationRequest paginationRequest = new PaginationRequest(from, size);
        return adminEventService.findAll(getEventsRequest, paginationRequest);
    }

    /**
     * Обновить событие
     * @param eventId id события
     * @param adminUpdateEventRequest параметры для обновления
     * @return DTO обновленного события
     */
    @PutMapping("/{eventId}")
    public AdminEventFullDto update(@PathVariable Long eventId,
                                    @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Admin request to update event with id = {}", eventId);
        return adminEventService.update(eventId, adminUpdateEventRequest);
    }

    /**
     * Опубликовать событие
     * @param eventId id события
     * @return DTO опубликованного события
     */
    @PatchMapping("/{eventId}/publish")
    public AdminEventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("Admin request to publish event with id = {}", eventId);
        return adminEventService.publish(eventId);
    }

    /**
     * Отклонить событие
     * @param eventId id события
     * @return DTO отклоненного события
     */
    @PatchMapping("/{eventId}/reject")
    public AdminEventFullDto rejectEvent(@PathVariable Long eventId) {
        log.info("Admin request to reject event with id = {}", eventId);
        return adminEventService.reject(eventId);
    }
}
