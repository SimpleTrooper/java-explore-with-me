package ru.practicum.explorewithme.apiprivate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.validation.groups.OnCreate;
import ru.practicum.explorewithme.base.validation.groups.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

/**
 * DTO для добавления/обновления события пользователем
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(groups = OnCreate.class)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank(groups = OnCreate.class)
    private String description;
    @NotNull(groups = OnCreate.class)
    private LocalDateTime eventDate;
    @NotNull(groups = OnCreate.class)
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    @Null(groups = OnUpdate.class)
    private Boolean requestModeration;
    @NotBlank(groups = OnCreate.class)
    private String title;
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long eventId;
}
