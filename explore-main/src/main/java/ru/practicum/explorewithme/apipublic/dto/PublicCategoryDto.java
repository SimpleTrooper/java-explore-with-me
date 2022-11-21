package ru.practicum.explorewithme.apipublic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Category;

/**
 * DTO категории - public API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicCategoryDto {
    private Long id;
    private String name;

    public static PublicCategoryDto from(Category category) {
        return new PublicCategoryDto(category.getId(), category.getName());
    }
}
