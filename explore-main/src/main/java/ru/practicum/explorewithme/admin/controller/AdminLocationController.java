package ru.practicum.explorewithme.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.admin.dto.AdminLocationDto;
import ru.practicum.explorewithme.admin.dto.NewLocationDto;
import ru.practicum.explorewithme.admin.service.AdminLocationService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.validation.groups.OnCreate;
import ru.practicum.explorewithme.base.validation.groups.OnUpdate;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер API администратора - локации
 */
@Slf4j
@RestController
@HandleExceptions
@Validated
@RequestMapping("/admin/locations")
public class AdminLocationController {
    private final AdminLocationService adminLocationService;

    @Autowired
    public AdminLocationController(AdminLocationService adminLocationService) {
        this.adminLocationService = adminLocationService;
    }

    /**
     * Получение всех локаций с фильтрацией по id и пагинацией (если отсутствует список id)
     *
     * @param locationIds id нужных событий
     * @param from        пагинация от
     * @param size        количество записей
     * @return список DTO полученных локаций
     */
    @GetMapping
    public List<AdminLocationDto> findAll(@RequestParam(required = false) List<Long> locationIds,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        log.info("Admin request to find all locations");
        PaginationRequest paginationRequest = new PaginationRequest(from, size);
        return adminLocationService.findAll(locationIds, paginationRequest);
    }

    /**
     * Добавление новой локации
     *
     * @param newLocation данные новой локации
     * @return DTO добавленной локации
     */
    @PostMapping
    @Validated(value = OnCreate.class)
    public AdminLocationDto add(@RequestBody @Valid NewLocationDto newLocation) {
        log.info("Admin request to add new location: {}", newLocation.toString());
        return adminLocationService.add(newLocation);
    }

    /**
     * Обновление параметров локации
     *
     * @param updatedLocation данные для обновления
     * @return DTO обновленной локации
     */
    @PatchMapping
    @Validated(value = OnUpdate.class)
    public AdminLocationDto update(@RequestBody @Valid NewLocationDto updatedLocation) {
        log.info("Admin request to update location with id = {}", updatedLocation.toString());
        return adminLocationService.update(updatedLocation);
    }

    /**
     * Удаление локации по id
     *
     * @param locationId id удаляемой локации
     */
    @DeleteMapping("/{locationId}")
    public void delete(@PathVariable Long locationId) {
        log.info("Admin request to delete location with id = {}", locationId);
        adminLocationService.delete(locationId);
    }

    /**
     * Получить и сохранить информацию о локации с сервиса геокодирования
     *
     * @param locationId id локации
     * @return DTO локации с полученной информацией
     */
    @PatchMapping("/{locationId}/resolve")
    public AdminLocationDto resolveById(@PathVariable Long locationId) {
        log.info("Admin request to resolve location with id = {}", locationId);
        return adminLocationService.resolveById(locationId);
    }
}
