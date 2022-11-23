package ru.practicum.explorewithme.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Событие с количеством просмотров
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventWithViews {
    private Event event;
    private Long views;
}
