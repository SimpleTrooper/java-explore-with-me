package ru.practicum.explorewithme.base.handler;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Модульные тесты для ApiError
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ApiErrorTest {

    /**
     * Стандартное поведение создания ApiError
     */
    @Test
    void shouldMakeApiErrorForBadRequest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = formatter.format(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        String reason = "reason";
        List<String> errors = List.of("error1", "error2");
        String message = "message";

        ApiError actual = ApiError.makeApiErrorWBadRequest(errors, reason, message);

        assertThat(actual, notNullValue());
        assertThat(actual.getErrors(), equalTo(errors));
        assertThat(actual.getReason(), equalTo(reason));
        assertThat(actual.getMessage(), equalTo(message));
        assertThat(actual.getTimestamp(), equalTo(timestamp));
        assertThat(actual.getStatus(), equalTo(HttpStatus.BAD_REQUEST.toString()));
    }
}