package ru.practicum.explorewithme.admin.service;

import ru.practicum.explorewithme.admin.dto.NewCategoryDto;

/**
 * Бизнес-логика категорий API администратора
 */
public interface AdminCategoryService {
    /**
     * Изменение категории
     *
     * @param newCategoryDto DTO для изменения
     * @return DTO измененной категории
     */
    NewCategoryDto update(NewCategoryDto newCategoryDto);

    /**
     * Добавление новой категории
     *
     * @param newCategoryDto DTO для добавления
     * @return DTO добавленной категории
     */
    NewCategoryDto add(NewCategoryDto newCategoryDto);

    /**
     * Удаление категории
     *
     * @param categoryId id удаляемой категории
     */
    void delete(Long categoryId);
}
