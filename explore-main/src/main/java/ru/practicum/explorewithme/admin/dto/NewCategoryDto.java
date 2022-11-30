package ru.practicum.explorewithme.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.validation.groups.OnCreate;
import ru.practicum.explorewithme.base.validation.groups.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * DTO создания/обновления категории администратором
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;
    @NotBlank(groups = OnCreate.class)
    private String name;

    public static NewCategoryDto from(Category category) {
        return new NewCategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(newCategoryDto.getId(), newCategoryDto.getName());
    }
}
