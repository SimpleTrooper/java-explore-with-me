package ru.practicum.exploreclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
