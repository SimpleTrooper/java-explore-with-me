package ru.practicum.exploregeocoding.service;

import ru.practicum.exploregeocoding.dto.InputLocation;
import ru.practicum.exploregeocoding.dto.OutputLocationDto;

/**
 * Бизнес логика геокодирования
 */
public interface GeocodingService {
    /**
     * Обратное геокодирования
     *
     * @param inputLocation информация для геокодирования
     * @return информация о запрошенной точке
     */
    OutputLocationDto reverseGeocode(InputLocation inputLocation);
}
