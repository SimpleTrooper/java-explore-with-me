package ru.practicum.explorewithme.apiprivate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.apiprivate.dto.NewEventDto;
import ru.practicum.explorewithme.apiprivate.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventFullDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventShortDto;
import ru.practicum.explorewithme.apiprivate.service.PrivateEventService;
import ru.practicum.explorewithme.apiprivate.service.PrivateRequestService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.validation.groups.OnCreate;
import ru.practicum.explorewithme.base.validation.groups.OnUpdate;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер приватного API - события
 */
@Slf4j
@HandleExceptions
@RestController
@Validated
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final PrivateEventService privateEventService;
    private final PrivateRequestService privateRequestService;

    public PrivateEventController(PrivateEventService privateEventService,
                                  PrivateRequestService privateRequestService) {
        this.privateEventService = privateEventService;
        this.privateRequestService = privateRequestService;
    }

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId id пользователя
     * @param from   пагинация - начиная с
     * @param size   количество записей
     * @return список коротких DTO событий
     */
    @GetMapping
    public List<PrivateEventShortDto> findAll(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("Private request to find all user's events with user id = {}", userId);
        return privateEventService.findAllByInitiatorId(userId, new PaginationRequest(from, size));
    }

    /**
     * Обновить событие
     *
     * @param userId      id пользователя-инициатора
     * @param newEventDto DTO обновления
     * @return полное DTO обновленного события
     */
    @PatchMapping
    @Validated(value = OnUpdate.class)
    public PrivateEventFullDto update(@PathVariable Long userId,
                                      @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Private request to update user's event with id = {} and user id = {}", newEventDto.getEventId(),
                userId);
        return privateEventService.update(userId, newEventDto);
    }

    /**
     * Добавить событие
     *
     * @param userId      id пользователя-инициатора
     * @param newEventDto DTO нового события
     * @return полное DTO нового события
     */
    @PostMapping
    @Validated(value = OnCreate.class)
    public PrivateEventFullDto add(@PathVariable Long userId,
                                   @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Private request to create new event for user with id = {}", userId);
        return privateEventService.add(userId, newEventDto);
    }

    /**
     * Получение полной информации о событии, добавленном текущим пользователем
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return Полное DTO события
     */
    @GetMapping("/{eventId}")
    public PrivateEventFullDto findById(@PathVariable Long userId,
                                        @PathVariable Long eventId) {
        log.info("Private request to find event with id = {} from user with id = {}", eventId, userId);
        return privateEventService.findById(userId, eventId);
    }

    /**
     * Отмена события, добавленного текущим пользователем
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return Полное DTO события
     */
    @PatchMapping("/{eventId}")
    public PrivateEventFullDto cancel(@PathVariable Long userId,
                                      @PathVariable Long eventId) {
        log.info("Private request to cancel event with id = {} from user with id = {}", eventId, userId);
        return privateEventService.cancel(userId, eventId);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return Список DTO запросов
     */
    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsForEvent(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        log.info("Private request to get all participation requests from user with id = {} for event with id = {}",
                userId, eventId);
        return privateRequestService.find(userId, eventId);
    }

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя
     *
     * @param userId    id пользователя
     * @param eventId   id события
     * @param requestId id запроса
     * @return DTO подтвержденного запроса
     */
    @PatchMapping("/{eventId}/requests/{requestId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long requestId) {
        log.info("Private request to confirm participation request with id = {} for event with id = {}",
                requestId, eventId);
        return privateRequestService.confirm(userId, eventId, requestId);
    }

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя
     *
     * @param userId    id пользователя
     * @param eventId   id события
     * @param requestId id запроса
     * @return DTO отклоненного запроса
     */
    @PatchMapping("/{eventId}/requests/{requestId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long requestId) {
        log.info("Private request to reject participation request with id = {} for event with id = {}",
                requestId, eventId);
        return privateRequestService.reject(userId, eventId, requestId);
    }
}
