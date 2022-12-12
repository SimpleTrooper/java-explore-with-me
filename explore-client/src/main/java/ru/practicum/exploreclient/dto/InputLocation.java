package ru.practicum.exploreclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Входные параметры для запроса на геокодирование
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputLocation {
    private Double lat;
    private Double lon;
    private String type;
}
