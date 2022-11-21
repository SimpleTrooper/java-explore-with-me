package ru.practicum.explorewithme.apipublic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.apipublic.dto.PublicCategoryDto;
import ru.practicum.explorewithme.apipublic.service.PublicCategoryService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Контроллер публичного API - категории
 */
@Slf4j
@HandleExceptions
@RestController
@RequestMapping("/categories")
public class PublicCategoryController {
    private final PublicCategoryService publicCategoryService;

    public PublicCategoryController(PublicCategoryService publicCategoryService) {
        this.publicCategoryService = publicCategoryService;
    }

    /**
     * Получение категорий
     * @param from пагинация - от
     * @param size пагинация - размер
     * @return Список DTO категорий
     */
    @GetMapping
    public List<PublicCategoryDto> findAll(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Public request to find all categories");
        return publicCategoryService.findAll(new PaginationRequest(from, size));
    }

    /**
     * Получение категории по id
     * @param categoryId id категории
     * @return DTO категории
     */
    @GetMapping("/{categoryId}")
    public PublicCategoryDto findById(@PathVariable Long categoryId) {
        log.info("Public request to find category with id = {}", categoryId);
        return publicCategoryService.findById(categoryId);
    }
}
