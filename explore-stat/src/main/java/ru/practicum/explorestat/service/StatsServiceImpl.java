package ru.practicum.explorestat.service;

import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreclient.dto.EndpointHit;
import ru.practicum.exploreclient.dto.ViewStats;
import ru.practicum.explorestat.dto.StatsDto;
import ru.practicum.explorestat.model.QStatistic;
import ru.practicum.explorestat.model.Statistic;
import ru.practicum.explorestat.repository.StatisticRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики сервиса статистики
 */
@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private StatisticRepository statisticRepository;

    public StatsServiceImpl(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public List<StatsDto> findAll(ViewStats viewStats) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(QStatistic.statistic.requestDate.goe(viewStats.getStart()));
        booleanBuilder.and(QStatistic.statistic.requestDate.loe(viewStats.getEnd()));

        List<String> uris = viewStats.getUris();
        if (uris != null && uris.size() != 0) {
            booleanBuilder.and(QStatistic.statistic.endpoint.in(uris));
        }

        return statisticRepository.findAllBy(booleanBuilder, viewStats.getUnique()).stream()
                .map(StatsDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void add(EndpointHit endpointHit) {
        Statistic statistic = Statistic.builder()
                .appId(endpointHit.getApp())
                .endpoint(endpointHit.getUri())
                .clientIp(endpointHit.getIp())
                .requestDate(endpointHit.getTimestamp())
                .build();
        statisticRepository.save(statistic);
    }
}
