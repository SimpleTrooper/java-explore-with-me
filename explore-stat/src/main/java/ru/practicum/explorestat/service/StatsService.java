package ru.practicum.explorestat.service;

import ru.practicum.exploreclient.dto.EndpointHit;
import ru.practicum.exploreclient.dto.ViewStats;
import ru.practicum.explorestat.dto.StatsDto;

import java.util.List;

/**
 * Бизнес-логика сервиса статистики
 */
public interface StatsService {
    /**
     * Получить статистику по посещениям
     * @param viewStats запрос с фильтрацией
     * @return список DTO со статистикой
     */
    List<StatsDto> findAll(ViewStats viewStats);

    /**
     * Добавить информацию о том, что к эндпоинту был запрос
     * @param endpointHit информация о запросе к эндпоинту
     */
    void add(EndpointHit endpointHit);
}
