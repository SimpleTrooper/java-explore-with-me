package ru.practicum.explorestat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorestat.model.StatsShortWHits;

/**
 * DTO для отправки статистики по эндпоинту
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {
    private String app;
    private String uri;
    private Long hits;

    public static StatsDto from(StatsShortWHits statsShortWHits) {
        return new StatsDto(statsShortWHits.getApp(), statsShortWHits.getUri(), statsShortWHits.getHits());
    }
}
