package ru.practicum.explorewithme.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.explorewithme.admin.dto.NewCategoryDto;
import ru.practicum.explorewithme.admin.service.AdminCategoryService;
import ru.practicum.explorewithme.base.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.repository.CategoryRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Интеграционные тесты для AdminCategoryServiceImpl
 */
@DataJpaTest
@Import(AdminCategoryServiceImpl.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AdminCategoryServiceImplTest {
    final CategoryRepository categoryRepository;
    final AdminCategoryService adminCategoryService;

    Category category;

    @BeforeEach
    void init() {
        category = new Category(null, "Test category");
        categoryRepository.save(category);
    }

    /**
     * Стандартное поведение update
     */
    @Test
    void shouldUpdate() {
        NewCategoryDto newCategoryDto = new NewCategoryDto(category.getId(), "New name");

        NewCategoryDto actual = adminCategoryService.update(newCategoryDto);

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), equalTo(newCategoryDto.getId()));
        assertThat(actual.getName(), equalTo(newCategoryDto.getName()));
    }

    /**
     * Стандартное поведение add
     */
    @Test
    void shouldAdd() {
        NewCategoryDto newCategoryDto = new NewCategoryDto(null, "New name");

        NewCategoryDto actual = adminCategoryService.add(newCategoryDto);

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), notNullValue());
        assertThat(actual.getName(), equalTo(newCategoryDto.getName()));
    }

    /**
     * Стандартное поведение delete
     */
    @Test
    void shouldDelete() {
        Long categoryId = category.getId();
        assertDoesNotThrow(() -> adminCategoryService.delete(categoryId));

        assertThrows(CategoryNotFoundException.class, () -> adminCategoryService.delete(categoryId));
    }
}