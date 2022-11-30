package ru.practicum.explorewithme.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.admin.dto.AdminCompilationDto;
import ru.practicum.explorewithme.admin.dto.NewCompilationDto;
import ru.practicum.explorewithme.admin.service.AdminCompilationService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;

import javax.validation.Valid;

/**
 * Контроллер API администратора - подборки событий
 */
@Slf4j
@HandleExceptions
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    public AdminCompilationController(AdminCompilationService adminCompilationService) {
        this.adminCompilationService = adminCompilationService;
    }

    /**
     * Добавление новой подборки
     *
     * @param newCompilationDto DTO новой подборки
     * @return DTO с информацией о новой подборке
     */
    @PostMapping
    public AdminCompilationDto add(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Admin request to add new compilation: {}", newCompilationDto);
        return adminCompilationService.add(newCompilationDto);
    }

    /**
     * Удаление подборки
     *
     * @param compilationId id удаляемой подборки
     */
    @DeleteMapping("/{compilationId}")
    public void delete(@PathVariable Long compilationId) {
        log.info("Admin request to delete compilation with id = {}", compilationId);
        adminCompilationService.delete(compilationId);
    }

    /**
     * Удаление события из подборки
     *
     * @param compilationId id подборки
     * @param eventId       id события
     */
    @DeleteMapping("/{compilationId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compilationId,
                                           @PathVariable Long eventId) {
        log.info("Admin request to delete event with id = {} from compilation with id = {}",
                eventId, compilationId);
        adminCompilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    /**
     * Добавление события в подборку
     *
     * @param compilationId id подборки
     * @param eventId       id события
     */
    @PatchMapping("/{compilationId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compilationId,
                                      @PathVariable Long eventId) {
        log.info("Admin request to add event with id = {} to compilation with id = {}",
                eventId, compilationId);
        adminCompilationService.addEventToCompilation(compilationId, eventId);
    }

    /**
     * Открепление подборки
     *
     * @param compilationId id подборки
     */
    @DeleteMapping("/{compilationId}/pin")
    public void unpinCompilation(@PathVariable Long compilationId) {
        log.info("Admin request to unpin compilation with id = {}", compilationId);
        adminCompilationService.unpinCompilation(compilationId);
    }

    /**
     * Прикрепление подборки
     *
     * @param compilationId id подборки
     */
    @PatchMapping("/{compilationId}/pin")
    public void pinCompilation(@PathVariable Long compilationId) {
        log.info("Admin request to unpin compilation with id = {}", compilationId);
        adminCompilationService.pinCompilation(compilationId);
    }
}
