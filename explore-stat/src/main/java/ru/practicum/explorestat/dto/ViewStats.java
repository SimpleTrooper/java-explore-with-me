package ru.practicum.explorestat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Запрос для вывода статистики
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uris;
    private Boolean unique;

    public ViewStats(String start, String end, List<String> uris, Boolean unique) {
        this.start = ExploreDateFormatter.toLocalDateTime(start);
        this.end = ExploreDateFormatter.toLocalDateTime(end);
        this.uris = uris;
        this.unique = unique;
    }
}
