package ru.practicum.explorestat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorestat.model.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Long>, StatisticRepositoryCustom {
}
