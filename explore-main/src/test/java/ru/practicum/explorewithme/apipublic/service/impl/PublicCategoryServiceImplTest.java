package ru.practicum.explorewithme.apipublic.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.explorewithme.apipublic.dto.PublicCategoryDto;
import ru.practicum.explorewithme.apipublic.service.PublicCategoryService;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PublicCategoryServiceImpl.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PublicCategoryServiceImplTest {
    final CategoryRepository categoryRepository;
    final PublicCategoryService publicCategoryService;

    Category category1;
    Category category2;

    @BeforeEach
    void init() {
        category1 = new Category(null, "Category 1");
        category2 = new Category(null, "Category 2");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    /**
     * Стандартное поведение findAll
     */
    @Test
    void shouldFindAll() {
        List<PublicCategoryDto> actual = publicCategoryService.findAll(new PaginationRequest(0, 10));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.get(0).getId(), equalTo(category1.getId()));
        assertThat(actual.get(0).getName(), equalTo(category1.getName()));
        assertThat(actual.get(1).getId(), equalTo(category2.getId()));
        assertThat(actual.get(1).getName(), equalTo(category2.getName()));
    }

    /**
     * Стандартное поведение findById
     */
    @Test
    void shouldFindById() {
        PublicCategoryDto actual = publicCategoryService.findById(category1.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), equalTo(category1.getId()));
        assertThat(actual.getName(), equalTo(category1.getName()));
    }
}