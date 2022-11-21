package ru.practicum.exploreclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.exploreclient.dto.EndpointHit;
import ru.practicum.exploreclient.dto.StatsDto;

import java.util.List;

/**
 * OpenFeign клиент для формирования запросов к микросервису статистики
 */
@PropertySource(value = "/feign.properties")
@FeignClient(value = "feignClientForStats", url = "${explore.remote-service.url}")
public interface FeignClientForStats {
    /**
     * Получить статистику
     * @param start начало периода, за который нужно выгрузить статистику
     * @param end конец периода, за который нужно выгрузить статистику
     * @param uris uri эндпоинтов, для которых нужно выгрузить статистику
     * @param unique выгружать статистику только по уникальным ip
     * @return список DTO со статистикой
     */
    @RequestMapping(method = RequestMethod.GET, value = "/stats")
    List<StatsDto> getStats(@RequestParam String start,
                            @RequestParam String end,
                            @RequestParam(required = false) List<String> uris,
                            @RequestParam(defaultValue = "false") Boolean unique);

    /**
     * Сохранить информацию о том, что к эндпоинту был запрос
     * @param endpointHit информация о запросе
     */
    @RequestMapping(method = RequestMethod.POST, value = "/hit")
    void hit(@RequestBody EndpointHit endpointHit);
}
