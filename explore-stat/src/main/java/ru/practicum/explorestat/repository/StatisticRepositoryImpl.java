package ru.practicum.explorestat.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Lazy;
import ru.practicum.explorestat.model.QStatistic;
import ru.practicum.explorestat.model.StatsShortWHits;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticRepositoryImpl implements StatisticRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public StatisticRepositoryImpl(@Lazy EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<StatsShortWHits> findAllBy(BooleanBuilder booleanBuilder, Boolean distinctIp) {
        NumberExpression<Long> distinctIpCount = QStatistic.statistic.clientIp.count();
        if (distinctIp) {
            distinctIpCount = QStatistic.statistic.clientIp.countDistinct();
        }
        List<Tuple> list = jpaQueryFactory.select(QStatistic.statistic.appId,
                QStatistic.statistic.endpoint,
                distinctIpCount)
                .from(QStatistic.statistic)
                .where(booleanBuilder)
                .groupBy(QStatistic.statistic.appId, QStatistic.statistic.endpoint)
                .fetch();
        return list.stream().map(tuple -> new StatsShortWHits(tuple.get(0, String.class),
                tuple.get(1, String.class), tuple.get(2, Long.class)))
                .collect(Collectors.toList());
    }
}
