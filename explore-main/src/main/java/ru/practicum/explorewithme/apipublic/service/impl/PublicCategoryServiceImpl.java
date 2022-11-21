package ru.practicum.explorewithme.apipublic.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.apipublic.dto.PublicCategoryDto;
import ru.practicum.explorewithme.apipublic.service.PublicCategoryService;
import ru.practicum.explorewithme.base.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики категорий
 */
@Service
@Transactional(readOnly = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository categoryRepository;

    public PublicCategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<PublicCategoryDto> findAll(PaginationRequest paginationRequest) {
        return categoryRepository.findAll(paginationRequest.makeOffsetBasedByFieldAsc("id")).stream()
                .map(PublicCategoryDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public PublicCategoryDto findById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id = %d is not found",
                        categoryId)));
        return PublicCategoryDto.from(category);
    }
}
