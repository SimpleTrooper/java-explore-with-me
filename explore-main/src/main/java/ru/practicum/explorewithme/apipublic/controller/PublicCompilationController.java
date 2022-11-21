package ru.practicum.explorewithme.apipublic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.apipublic.dto.PublicCompilationDto;
import ru.practicum.explorewithme.apipublic.service.PublicCompilationService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Контроллер публичного API - подборки событий
 */
@Slf4j
@HandleExceptions
@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    public PublicCompilationController(PublicCompilationService publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

    /**
     *  Получение подборок событий
     * @param pinned показать закрепленные/не закрепленные подборки
     *               true = {"true", "on", "yes", "1"}, false = {"false", "off", "no", "0"}
     * @param from пагинация - записи, начиная с from
     * @param size пагинация - количество записей
     * @return Список найденных DTO подборок
     */
    @GetMapping
    public List<PublicCompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("Public request to find all compilations");
        return publicCompilationService.findAll(pinned, new PaginationRequest(from, size));
    }

    /**
     * Получение подборки по её id
     * @param compilationId id подборки
     * @return DTO найденной подборки
     */
    @GetMapping("/{compilationId}")
    public PublicCompilationDto findById(@PathVariable Long compilationId) {
        log.info("Public request to find compilation with id = {}", compilationId);
        return publicCompilationService.findById(compilationId);
    }
}
