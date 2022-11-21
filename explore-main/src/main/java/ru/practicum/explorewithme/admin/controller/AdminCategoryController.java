package ru.practicum.explorewithme.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.admin.dto.NewCategoryDto;
import ru.practicum.explorewithme.admin.service.AdminCategoryService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.validation.groups.OnCreate;
import ru.practicum.explorewithme.base.validation.groups.OnUpdate;

import javax.validation.Valid;

/**
 * Контроллер API администратора - категории
 */
@Slf4j
@HandleExceptions
@RestController
@Validated
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @Autowired
    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    /**
     * Изменение категории
     * @param newCategoryDto DTO для изменения
     * @return DTO измененной категории
     */
    @PatchMapping
    @Validated(value = OnUpdate.class)
    public NewCategoryDto update(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Admin request to update category with id={}", newCategoryDto.getId());
        return adminCategoryService.update(newCategoryDto);
    }

    /**
     * Добавление новой категории
     * @param newCategoryDto DTO для добавления
     * @return DTO добавленной категории
     */
    @PostMapping
    @Validated(value = OnCreate.class)
    public NewCategoryDto add(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Admin request to add new category: {}", newCategoryDto);
        return adminCategoryService.add(newCategoryDto);
    }

    /**
     * Удаление категории
     * @param categoryId id удаляемой категории
     */
    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable Long categoryId) {
        log.info("Admin request to delete category with id = {}", categoryId);
        adminCategoryService.delete(categoryId);
    }
}
