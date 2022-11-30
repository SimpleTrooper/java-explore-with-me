package ru.practicum.explorestat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *  Тело запроса для сохранения информации о том, что к эндпоинту был запрос
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHit {
    Long id;
    String app;
    String uri;
    String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
