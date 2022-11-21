package ru.practicum.explorestat.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.explorestat.controller.StatsController;
import ru.practicum.explorestat.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Общий обработчик исключений
 */
@Slf4j
@ControllerAdvice(basePackageClasses = {StatsController.class})
public class ErrorHandler {

    /**
     * Обработчик исключения при неверной валидации для MethodArgumentNotValidException
     *
     * @param ex MethodArgumentNotValidException
     * @return описание ошибки, код 400
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> validationHandler(final MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() +
                        ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        String reason = "Validation error.";
        log.error(reason + errors);

        ApiError error = ApiError.makeApiErrorWBadRequest(errors, reason, null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик исключения при неверной валидации для MissingServletRequestParameterException
     * @param ex MissingServletRequestParameterException
     * @return описание ошибки, код 400
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> validationHandler(final MissingServletRequestParameterException ex) {
        String reason = "Validation error.";
        String message = ex.getMessage();
        log.error(reason + message);

        ApiError error = ApiError.makeApiErrorWBadRequest(null, reason, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик исключения при неверной валидации для HttpMessageNotReadableException
     * @param ex HttpMessageNotReadableException
     * @return описание ошибки, код 400
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> validationHandler(final HttpMessageNotReadableException ex) {
        String reason = "Validation error.";
        String message = ex.getMessage();
        log.error(reason + message);

        ApiError error = ApiError.makeApiErrorWBadRequest(null, reason, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * Обработчик исключения при неверной валидации для DataAccessException
     * @param ex DataAccessException
     * @return описание ошибки, код 400
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> validationHandler(final DataAccessException ex) {
        String reason = "Validation error.";
        String message = ex.getMessage();
        log.error(reason + message);

        ApiError error = ApiError.makeApiErrorWBadRequest(null, reason, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик исключений при нарушении ограничений в БД
     *
     * @param ex DataIntegrityViolationException
     * @return описание ошибки, код 409
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> integrityConstraintHandler(final DataIntegrityViolationException ex) {
        String reason = "Integrity constraint has been violated";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        log.error(reason + ex.getMessage());
        ApiError error = ApiError.builder()
                .reason(reason)
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(ExploreDateFormatter.format(timestamp))
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Обработчик всех не обработанных исключений
     *
     * @param ex Throwable
     * @return описание ошибки, код 500
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> allHandler(final Throwable ex) {
        String reason = "Internal server error";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        log.error(reason + ex.getMessage());
        ApiError error = ApiError.builder()
                .reason(reason)
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(ExploreDateFormatter.format(timestamp))
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
