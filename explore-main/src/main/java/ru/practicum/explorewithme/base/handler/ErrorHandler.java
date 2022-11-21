package ru.practicum.explorewithme.base.handler;

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
import ru.practicum.explorewithme.base.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.base.exception.NotFoundException;
import ru.practicum.explorewithme.base.exception.WrongStateException;
import ru.practicum.explorewithme.base.util.ExploreDateFormatter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Общий обработчик исключений
 */
@Slf4j
@ControllerAdvice(annotations = HandleExceptions.class)
public class ErrorHandler {
    /**
     * Обработчик исключений не найденной записи
     *
     * @param ex NotFoundException
     * @return описание ошибки, код 404
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> notFoundHandler(final NotFoundException ex) {
        String reason = "Entity is not found.";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        log.error(reason + ex.getMessage());
        ApiError error = ApiError.builder()
                .reason(reason)
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(ExploreDateFormatter.format(timestamp))
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик исключений неверного статуса в запросе
     *
     * @param ex WrongStateException
     * @return описание ошибки, код 400
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> wrongStateHandler(final WrongStateException ex) {
        String reason = "Wrong state in request.";
        String message = ex.getMessage();
        log.error(reason + message);
        ApiError error = ApiError.makeApiErrorWBadRequest(null, reason, message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

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
     * Обработчик исключения при неверной валидации для ConstraintViolationException
     * @param ex ConstraintViolationException
     * @return описание ошибки, код 400
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> validationHandler(final ConstraintViolationException ex) {
        String reason = "Constraint violation error.";
        List<String> errors = ex.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
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
     * Обработчик исключений при невыполненных условий для запроса
     *
     * @param ex ConditionsNotMetException
     * @return описание ошибки, код 403
     */
    @ExceptionHandler
    public ResponseEntity<ApiError> conditionsAreNotMetHandler(final ConditionsNotMetException ex) {
        String reason = "For the requested operation conditions are not met.";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        log.error(reason + ex.getMessage());
        ApiError error = ApiError.builder()
                .reason(reason)
                .message(ex.getMessage())
                .status(HttpStatus.FORBIDDEN.toString())
                .timestamp(ExploreDateFormatter.format(timestamp))
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
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
