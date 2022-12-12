package ru.practicum.exploreclient;

import ru.practicum.exploreclient.dto.EndpointHit;

import java.util.Collection;
import java.util.Map;

/**
 * Клиент для обмена данными между микросервисами
 */
public interface ExploreClientForStats {
    /**
     * Получение количества просмотров для событий по списку их id
     * @param eventIds id
     * @return id события, количество просмотров
     */
    Map<Long, Long> getViewsForEvents(Collection<Long> eventIds);

    /**
     * Сохранение информации о том, что к эндпоинту был запрос
     * @param endpointHit DTO с информацией о запросе
     */
    void hit(EndpointHit endpointHit);
}
