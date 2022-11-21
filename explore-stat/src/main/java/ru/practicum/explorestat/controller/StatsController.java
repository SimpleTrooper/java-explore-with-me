package ru.practicum.explorestat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exploreclient.dto.EndpointHit;
import ru.practicum.exploreclient.dto.ViewStats;
import ru.practicum.explorestat.dto.StatsDto;
import ru.practicum.explorestat.service.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Основной контроллер статистики
 */
@Slf4j
@RestController
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * Сохранение информации о том, что к эндоинту был запрос
     * @param endpointHit информация о запросе к эндпоинту
     */
    @PostMapping("/hit")
    public void hit(@RequestBody EndpointHit endpointHit) {
        log.info("Request to hit for endpoint with uri = {}", endpointHit.getUri());
        statsService.add(endpointHit);
    }

    /**
     * Получение статистики по посещениям
     * @param start начало диапазона за который нужно выгрузить статистику
     * @param end конец диапазона за который нужно выгрузить статистику
     * @param uris список uri для которых нужно выгрузить статистику
     * @param unique учитывать посещения только с уникальных ip
     * @return список DTO со статистикой
     */
    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Request to get stats");
        String startDecoded = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String endDecoded = URLDecoder.decode(end, StandardCharsets.UTF_8);
        ViewStats viewStats = new ViewStats(startDecoded, endDecoded, uris, unique);
        return statsService.findAll(viewStats);
    }
}
