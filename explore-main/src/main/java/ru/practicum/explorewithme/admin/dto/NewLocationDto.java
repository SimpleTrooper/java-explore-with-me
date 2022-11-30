package ru.practicum.explorewithme.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.model.LocationType;
import ru.practicum.explorewithme.base.validation.groups.OnCreate;
import ru.practicum.explorewithme.base.validation.groups.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * DTO для создания/обновления локации
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewLocationDto {
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;
    @NotBlank(groups = OnCreate.class)
    private String type;
    @NotNull(groups = OnCreate.class)
    private Double lat;
    @NotNull(groups = OnCreate.class)
    private Double lon;
    @NotNull(groups = OnCreate.class)
    private Double radius;
    @NotBlank(groups = OnCreate.class)
    private String name;
    @NotBlank(groups = OnCreate.class)
    private String description;

    public static Location toLocation(NewLocationDto newLocationDto) {
        return Location.builder()
                .id(newLocationDto.id)
                .type(LocationType.from(newLocationDto.type))
                .lat(newLocationDto.lat)
                .lon(newLocationDto.lon)
                .radius(newLocationDto.radius)
                .name(newLocationDto.name)
                .description(newLocationDto.description)
                .build();
    }
}
