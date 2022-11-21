package ru.practicum.explorewithme.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.admin.dto.NewCategoryDto;
import ru.practicum.explorewithme.admin.service.AdminCategoryService;
import ru.practicum.explorewithme.base.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.repository.CategoryRepository;

/**
 * Реализация бизнес-логики категорий
 */
@Service
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public AdminCategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public NewCategoryDto update(NewCategoryDto newCategoryDto) {
        Category category = findCategoryByIdOrThrow(newCategoryDto.getId());
        updateRequiredFields(category, newCategoryDto);
        return NewCategoryDto.from(category);
    }

    private void updateRequiredFields(Category category, NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() != null) {
            category.setName(newCategoryDto.getName());
        }
    }

    @Override
    @Transactional
    public NewCategoryDto add(NewCategoryDto newCategoryDto) {
        Category newCategory = NewCategoryDto.toCategory(newCategoryDto);
        categoryRepository.save(newCategory);
        return NewCategoryDto.from(newCategory);
    }

    @Override
    @Transactional
    public void delete(Long categoryId) {
        findCategoryByIdOrThrow(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private Category findCategoryByIdOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new CategoryNotFoundException(String.format("Category with id = %d is not found", id)));
    }
}
