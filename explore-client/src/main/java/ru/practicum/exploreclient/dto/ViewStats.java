package ru.practicum.exploreclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ViewStats(String start, String end, List<String> uris, Boolean unique) {
        this.start = LocalDateTime.parse(start, dateFormatter);
        this.end = LocalDateTime.parse(end, dateFormatter);
        this.uris = uris;
        this.unique = unique;
    }
}
