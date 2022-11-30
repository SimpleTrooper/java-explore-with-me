package ru.practicum.exploregeocoding.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Класс ответа клиенту в случае ошибки
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Jacksonized
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
