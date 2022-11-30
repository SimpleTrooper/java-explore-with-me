package ru.practicum.explorestat.repository;

import com.querydsl.core.BooleanBuilder;
import ru.practicum.explorestat.model.StatsShortWHits;

import java.util.List;

public interface StatisticRepositoryCustom {
    List<StatsShortWHits> findAllBy(BooleanBuilder booleanBuilder, Boolean distinctIp);
}
