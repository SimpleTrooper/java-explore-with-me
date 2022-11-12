package ru.practicum.explorewithme.apipublic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
