package ru.practicum.explorestat.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.exploreclient.dto.EndpointHit;
import ru.practicum.exploreclient.dto.ViewStats;
import ru.practicum.explorestat.dto.StatsDto;
import ru.practicum.explorestat.model.Statistic;
import ru.practicum.explorestat.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Интеграционные тесты для StatsServiceImpl
 */
@DataJpaTest
@Import(StatsServiceImpl.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatsServiceImplTest {
    final StatisticRepository statisticRepository;
    final StatsService statsService;

    Statistic statistic1;
    Statistic statistic2;
    Statistic statistic3;
    Statistic statistic4;

    String appId = "Test app";
    String endpoint1 = "/events";
    String endpoint2 = "/events/1";

    @BeforeEach
    void init() {
        statistic1 = Statistic.builder()
                .appId(appId)
                .endpoint(endpoint1)
                .clientIp("127.0.0.1")
                .requestDate(LocalDateTime.now().minusMinutes(10).truncatedTo(ChronoUnit.SECONDS))
                .build();
        statistic2 = Statistic.builder()
                .appId(appId)
                .endpoint(endpoint1)
                .clientIp("127.0.0.1")
                .requestDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        statistic3 = Statistic.builder()
                .appId(appId)
                .endpoint(endpoint1)
                .clientIp("127.0.0.2")
                .requestDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        statistic4 = Statistic.builder()
                .appId(appId)
                .endpoint(endpoint2)
                .clientIp("127.0.0.3")
                .requestDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        statisticRepository.save(statistic1);
        statisticRepository.save(statistic2);
        statisticRepository.save(statistic3);
        statisticRepository.save(statistic4);
    }

    /**
     * Стандартное поведение findAll
     */
    @Test
    void shouldFindAll() {
        ViewStats viewStats = new ViewStats(LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1),
                null,
                false);

        List<StatsDto> actual = statsService.findAll(viewStats);

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.get(0).getApp(), equalTo(appId));
        assertThat(actual.get(0).getUri(), equalTo(endpoint1));
        assertThat(actual.get(0).getHits(), equalTo(3L));
        assertThat(actual.get(1).getApp(), equalTo(appId));
        assertThat(actual.get(1).getUri(), equalTo(endpoint2));
        assertThat(actual.get(1).getHits(), equalTo(1L));
    }

    /**
     * Стандартное поведение findAll distinct = true
     */
    @Test
    void shouldFindAllDistinctIp() {
        ViewStats viewStats = new ViewStats(LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1),
                null,
                true);

        List<StatsDto> actual = statsService.findAll(viewStats);

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.get(0).getApp(), equalTo(appId));
        assertThat(actual.get(0).getUri(), equalTo(endpoint1));
        assertThat(actual.get(0).getHits(), equalTo(2L));
        assertThat(actual.get(1).getApp(), equalTo(appId));
        assertThat(actual.get(1).getUri(), equalTo(endpoint2));
        assertThat(actual.get(1).getHits(), equalTo(1L));
    }

    /**
     * Стандартное поведение findAll для даты = 5 минут назад
     */
    @Test
    void shouldFindAllForStartDate5MinBeforeNow() {
        ViewStats viewStats = new ViewStats(LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusHours(1),
                null,
                false);

        List<StatsDto> actual = statsService.findAll(viewStats);

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.get(0).getApp(), equalTo(appId));
        assertThat(actual.get(0).getUri(), equalTo(endpoint1));
        assertThat(actual.get(0).getHits(), equalTo(2L));
        assertThat(actual.get(1).getApp(), equalTo(appId));
        assertThat(actual.get(1).getUri(), equalTo(endpoint2));
        assertThat(actual.get(1).getHits(), equalTo(1L));
    }

    /**
     * Стандартное поведение add
     */
    @Test
    void shouldAdd() {
        EndpointHit endpointHit = new EndpointHit(null,
                "New app",
                "/events/new",
                "127.0.0.50",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        ViewStats viewStats = new ViewStats(LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusHours(1),
                List.of(endpointHit.getUri()),
                false);

        statsService.add(endpointHit);

        List<StatsDto> actual = statsService.findAll(viewStats);

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getApp(), equalTo(endpointHit.getApp()));
        assertThat(actual.get(0).getUri(), equalTo(endpointHit.getUri()));
        assertThat(actual.get(0).getHits(), equalTo(1L));
    }

}