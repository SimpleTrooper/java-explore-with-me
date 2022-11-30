package ru.practicum.explorewithme.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.LocationCoordinates;

import java.time.LocalDateTime;

/**
 * Тело запроса на обновление события администратором
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationCoordinates location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
