package ru.practicum.explorewithme.admin.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Параметры запроса /admin/events
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminGetEventsRequest {
    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private Long location;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
