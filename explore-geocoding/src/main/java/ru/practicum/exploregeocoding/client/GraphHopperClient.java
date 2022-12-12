package ru.practicum.exploregeocoding.client;

import org.springframework.cloud.openfeign.CollectionFormat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.exploregeocoding.dto.ReverseGeocodeAnswer;

import java.util.List;


/**
 * Open feign клиент для обращения к сервису геокодирования
 */
@FeignClient(name = "feignClientForGraphHopper", url = "${graphhopper.url}")
@CollectionFormat(feign.CollectionFormat.CSV)
public interface GraphHopperClient {
    /**
     * Запрос на геокодирование
     * @param key api ключ
     * @param point координаты точки
     * @param reverse true = обратное геокодирование
     * @return список с информацией о запрошенном месте
     */
    @RequestMapping(method = RequestMethod.GET, value = "/geocode")
    ReverseGeocodeAnswer geocode(@RequestParam String key,
                                 @RequestParam List<Double> point,
                                 @RequestParam Boolean reverse);
}
