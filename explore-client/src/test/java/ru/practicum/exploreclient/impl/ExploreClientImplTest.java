package ru.practicum.exploreclient.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.exploreclient.ExploreClient;
import ru.practicum.exploreclient.FeignClientForStats;
import ru.practicum.exploreclient.dto.StatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Модульные тесты для ExploreClientImpl
 */
@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(ExploreClientImpl.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@MockitoSettings(strictness = Strictness.WARN)
class ExploreClientImplTest {
    @MockBean
    final FeignClientForStats feignClientForStats;
    @InjectMocks
    final ExploreClient exploreClient;

    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final int daysPeriod = 30;

    @Test
    void shouldGetViewsForEvents() {
        Long event1Id = 1L;
        Long event2Id = 2L;
        Long event1Hits = 5L;
        Long event2Hits = 10L;
        List<Long> eventIds = List.of(event1Id, event2Id);
        final String startRange = LocalDateTime.now().minusDays(daysPeriod).format(dateTimeFormatter);
        final String endRange = LocalDateTime.now().format(dateTimeFormatter);
        final String startEncoded = URLEncoder.encode(startRange, StandardCharsets.UTF_8);
        final String endEncoded = URLEncoder.encode(endRange, StandardCharsets.UTF_8);
        final List<String> uris = eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList());

        StatsDto event1Stats = new StatsDto("app", uris.get(0), event1Hits);
        StatsDto event2Stats = new StatsDto("app", uris.get(1), event2Hits);
        Mockito.when(feignClientForStats.getStats(startEncoded, endEncoded, uris, true))
                .thenReturn(List.of(event1Stats, event2Stats));

        Map<Long, Long> actual = exploreClient.getViewsForEvents(eventIds);

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(eventIds.size()));
        assertThat(actual.get(event1Id), equalTo(event1Hits));
        assertThat(actual.get(event2Id), equalTo(event2Hits));

        Mockito.verify(feignClientForStats, times(1))
                .getStats(startEncoded, endEncoded, uris, true);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(feignClientForStats);
    }
}