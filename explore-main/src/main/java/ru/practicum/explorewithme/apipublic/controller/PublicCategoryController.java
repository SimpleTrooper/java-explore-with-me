package ru.practicum.explorewithme.apipublic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.apipublic.dto.CategoryDto;

import java.util.List;

/**
 * Контроллер публичного API - категории
 */
@Slf4j
@RestController
@RequestMapping("/categories")
public class PublicCategoryController {
    @GetMapping
    public List<CategoryDto> findAll(@RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return null;
    }

    @GetMapping("/{categoryId}")
    public CategoryDto findById(@PathVariable Long categoryId) {
        return null;
    }
}
