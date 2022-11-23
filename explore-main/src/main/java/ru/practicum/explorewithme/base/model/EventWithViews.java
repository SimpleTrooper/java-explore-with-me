package ru.practicum.explorewithme.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Событие с подтвержденными запросами и просмотрами
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventWithReqAndViews {
    private Event event;
    private Long confirmedRequests;
    private Long views;
}
