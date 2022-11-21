package ru.practicum.explorewithme.apiprivate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.apiprivate.validation.EventDateCorrect;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.model.User;
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
@Builder
public class NewEventDto {
    @NotBlank(groups = OnCreate.class)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank(groups = OnCreate.class)
    private String description;
    @NotNull(groups = OnCreate.class)
    @EventDateCorrect
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator) {
        return Event.builder()
                .annotation(newEventDto.annotation)
                .category(category)
                .description(newEventDto.description)
                .eventDate(newEventDto.eventDate)
                .location(newEventDto.location)
                .paid(newEventDto.paid)
                .participantLimit(newEventDto.participantLimit)
                .requestModeration(newEventDto.requestModeration)
                .title(newEventDto.title)
                .id(newEventDto.eventId)
                .initiator(initiator)
                .build();
    }
}
