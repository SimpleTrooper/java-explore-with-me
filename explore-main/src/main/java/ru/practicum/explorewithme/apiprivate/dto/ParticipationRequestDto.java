package ru.practicum.explorewithme.apiprivate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Request;
import ru.practicum.explorewithme.base.model.RequestState;
import ru.practicum.explorewithme.base.util.ExploreDateFormatter;

/**
 * DTO запроса на участие в событии
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestState status;

    public static ParticipationRequestDto from(Request request) {
        return new ParticipationRequestDto(ExploreDateFormatter.format(request.getCreatedOn()), request.getEvent().getId(),
                request.getId(), request.getRequester().getId(), request.getRequestState());
    }
}
