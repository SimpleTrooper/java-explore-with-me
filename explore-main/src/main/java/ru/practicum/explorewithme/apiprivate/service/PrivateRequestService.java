package ru.practicum.explorewithme.apiprivate.service;

import ru.practicum.explorewithme.apiprivate.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Бизнес-логика запросов на участие
 */
public interface PrivateRequestService {
    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * @param userId id пользователя
     * @param eventId id события
     * @return Список DTO запросов
     */
    List<ParticipationRequestDto> find(Long userId, Long eventId);

    /**
     * Подтверждение чужого запроса на участие в событии текущего пользователя
     * @param userId id пользователя
     * @param eventId id события
     * @param requestId id заявки на участие
     * @return DTO подтвержденного запроса
     */
    ParticipationRequestDto confirm(Long userId, Long eventId, Long requestId);

    /**
     * Отклонение чужого запроса на участие в событии текущего пользователя
     * @param userId id пользователя
     * @param eventId id события
     * @param requestId id заявки на участие
     * @return DTO подтвержденного запроса
     */
    ParticipationRequestDto reject(Long userId, Long eventId, Long requestId);

    /**
     * Получение информации о запросах текущего пользователя на участие в чужих событиях
     * @param userId id пользователя
     * @return Список DTO запросов
     */
    List<ParticipationRequestDto> findByUserId(Long userId);

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     * @param userId id пользователя
     * @param eventId id события
     * @return DTO запроса
     */
    ParticipationRequestDto add(Long userId, Long eventId);

    /**
     * Отмена своего запроса на участие в событии
     * @param userId id пользователя
     * @param requestId id запроса
     * @return DTO запроса
     */
    ParticipationRequestDto cancel(Long userId, Long requestId);
}
