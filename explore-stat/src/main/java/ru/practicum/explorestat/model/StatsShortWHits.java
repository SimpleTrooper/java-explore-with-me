package ru.practicum.explorestat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Короткая версия статистики с количеством просмотров
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatsShortWHits {
    private String app;
    private String uri;
    private Long hits;
}
