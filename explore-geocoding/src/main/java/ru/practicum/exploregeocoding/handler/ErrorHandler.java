package ru.practicum.exploregeocoding.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exploregeocoding.controller.GeocodingController;
import ru.practicum.exploregeocoding.exception.ClientException;
import ru.practicum.util.ExceptionParser;
import ru.practicum.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

/**
 * Общий обработчик ошибок
 */
@Slf4j
@RestControllerAdvice(assignableTypes = {GeocodingController.class})
public class ErrorHandler {
    /**
     * Обработчик для ClientException
     *
     * @param clientException входящее исключение
     * @return ответ с описанием ошибки
     */
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ApiError> clientExceptionHandle(final ClientException clientException) {
        String reason = "Feign client error.";
        log.error(reason + clientException.getMessage(), clientException);
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ApiError error = ApiError.builder()
                .errors(Collections.singletonList(ExceptionParser.makeStringFromStackTrace(clientException)))
                .reason(reason)
                .message(clientException.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(ExploreDateFormatter.format(timestamp))
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработчик для Throwable
     *
     * @param internalServerErrorException входящее исключение
     * @return ответ с описанием ошибки
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> serverErrorExceptionHandle(final Throwable internalServerErrorException) {
        String reason = "Internal server error.";
        log.error(reason + internalServerErrorException.getMessage(), internalServerErrorException);
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ApiError error = ApiError.builder()
                .errors(Collections.singletonList(ExceptionParser
                        .makeStringFromStackTrace(internalServerErrorException)))
                .reason(reason)
                .message(internalServerErrorException.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(ExploreDateFormatter.format(timestamp))
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
