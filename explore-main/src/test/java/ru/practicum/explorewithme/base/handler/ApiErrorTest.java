package ru.practicum.explorewithme.base.handler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.practicum.explorewithme.base.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Модульные тесты для ApiError
 */
@SpringBootTest
class ApiErrorTest {
    /**
     * Стандартное поведение создания ApiError
     */
    @Test
    void shouldMakeApiErrorForBadRequest() {
        String timestamp = ExploreDateFormatter.format(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
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