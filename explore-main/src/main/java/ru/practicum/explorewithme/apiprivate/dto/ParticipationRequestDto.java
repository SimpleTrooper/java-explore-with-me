package ru.practicum.explorewithme.apiprivate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.RequestState;

import java.time.LocalDateTime;

/**
 * DTO запроса на участие в событии
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestState status;
}
