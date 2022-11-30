package ru.practicum.exploregeocoding.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exploregeocoding.dto.InputLocation;
import ru.practicum.exploregeocoding.dto.OutputLocationDto;
import ru.practicum.exploregeocoding.service.GeocodingService;

/**
 * Контроллер геокодирования
 */
@Slf4j
@RestController
public class GeocodingController {
    private final GeocodingService geocodingService;

    @Autowired
    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    /**
     * Обратное геокодирование
     *
     * @param lat  широта
     * @param lon  долгота
     * @param type тип локации
     * @return информация о локации
     */
    @GetMapping("/geocode")
    public OutputLocationDto reverseGeocode(@RequestParam Double lat,
                                            @RequestParam Double lon,
                                            @RequestParam String type) {
        log.info("Reverse geocoding request for lat = {} and lon = {}", lat, lon);
        InputLocation inputLocation = new InputLocation(lat, lon, type);
        return geocodingService.reverseGeocode(inputLocation);
    }
}
