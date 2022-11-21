package ru.practicum.explorewithme.apipublic.service;

import ru.practicum.explorewithme.apipublic.dto.PublicCategoryDto;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Бизнес-логика категорий публичного API
 */
public interface PublicCategoryService {
    /**
     * Получение категорий
     * @param paginationRequest запрос пагинации
     * @return Список DTO полученных категорий
     */
    List<PublicCategoryDto> findAll(PaginationRequest paginationRequest);

    /**
     * Получение категории по id
     * @param categoryId id категории
     * @return DTO полученной категории
     */
    PublicCategoryDto findById(Long categoryId);
}

