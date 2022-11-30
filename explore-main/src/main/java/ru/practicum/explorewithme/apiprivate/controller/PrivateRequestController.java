package ru.practicum.explorewithme.apiprivate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.apiprivate.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.apiprivate.service.PrivateRequestService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;

import java.util.List;

/**
 * Контроллер приватного API - запросы на участие
 */
@Slf4j
@HandleExceptions
@RestController
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {
    private final PrivateRequestService privateRequestService;

    public PrivateRequestController(PrivateRequestService privateRequestService) {
        this.privateRequestService = privateRequestService;
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     *
     * @param userId id пользователя
     * @return Список DTO запросов на участие
     */
    @GetMapping
    public List<ParticipationRequestDto> findAllByRequester(@PathVariable Long userId) {
        log.info("Private request to find all participation requests for user with id = {}", userId);
        return privateRequestService.findByUserId(userId);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return DTO запроса
     */
    @PostMapping
    public ParticipationRequestDto add(@PathVariable Long userId,
                                       @RequestParam Long eventId) {
        log.info("Private request to add new participation request from user with id = {} for event with id = {}",
                userId, eventId);
        return privateRequestService.add(userId, eventId);
    }

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId    id пользователя
     * @param requestId id запроса
     * @return DTO отмененного запроса
     */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        log.info("Private request to cancel participation from user with id = {} for request with id = {}",
                userId, requestId);
        return privateRequestService.cancel(userId, requestId);
    }
}
