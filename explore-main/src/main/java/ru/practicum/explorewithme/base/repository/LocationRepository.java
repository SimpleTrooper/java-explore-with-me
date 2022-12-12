package ru.practicum.explorewithme.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.base.model.Location;

import java.util.List;

/**
 * Репозиторий локаций
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByIdIn(List<Long> locationIds);
}
