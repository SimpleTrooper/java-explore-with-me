package ru.practicum.explorestat.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import ru.practicum.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Класс ответа клиенту в случае ошибки
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

    public static ApiError makeApiErrorWBadRequest(List<String> errors, String reason, String message) {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        return ApiError.builder()
                .reason(reason)
                .errors(errors)
                .message(message)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(ExploreDateFormatter.format(timestamp))
                .build();
    }
}
