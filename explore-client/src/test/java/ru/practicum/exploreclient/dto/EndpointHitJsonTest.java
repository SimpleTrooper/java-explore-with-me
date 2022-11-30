package ru.practicum.exploreclient.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Json-test для EndpointHit
 */
@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EndpointHitJsonTest {
    final JacksonTester<EndpointHit> json;
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void shouldSerialize() throws Exception {
        EndpointHit endpointHit = EndpointHit.builder()
                .id(1L)
                .ip("127.0.0.1")
                .app("App")
                .uri("/events")
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        JsonContent<EndpointHit> actual = json.write(endpointHit);

        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(endpointHit.getId().intValue());
        assertThat(actual).extractingJsonPathStringValue("$.app").isEqualTo(endpointHit.getApp());
        assertThat(actual).extractingJsonPathStringValue("$.uri").isEqualTo(endpointHit.getUri());
        assertThat(actual).extractingJsonPathStringValue("$.ip").isEqualTo(endpointHit.getIp());
        assertThat(actual).extractingJsonPathStringValue("$.timestamp").isEqualTo(endpointHit.getTimestamp()
                .format(dateTimeFormatter));
    }
}