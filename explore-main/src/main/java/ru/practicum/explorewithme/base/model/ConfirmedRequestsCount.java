package ru.practicum.explorewithme.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс подтвержденных запросов на событие
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedRequestsCount {
    private Long eventId;
    private Long confirmedCount;
}
