package ru.practicum.explorewithme.apipublic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.apipublic.dto.PublicLocationDto;
import ru.practicum.explorewithme.apipublic.service.PublicLocationService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Контроллер публичного API - локации
 */
@Slf4j
@RestController
@HandleExceptions
@RequestMapping("/locations")
public class PublicLocationController {
    private final PublicLocationService publicLocationService;

    @Autowired
    public PublicLocationController(PublicLocationService publicLocationService) {
        this.publicLocationService = publicLocationService;
    }

    /**
     * Получение всех событий с пагинацией
     *
     * @param from пагинация от
     * @param size количество записей
     * @return список DTO локаций
     */
    @GetMapping
    public List<PublicLocationDto> findAll(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Public request to find all locations");
        return publicLocationService.findAll(new PaginationRequest(from, size));
    }

    /**
     * Получение локации по id
     *
     * @param locationId id локации
     * @return DTO локации
     */
    @GetMapping("/{locationId}")
    public PublicLocationDto findById(@PathVariable Long locationId) {
        log.info("Public request to find location by id = {}", locationId);
        return publicLocationService.findById(locationId);
    }
}
