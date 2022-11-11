package ru.practicum.explorewithme.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.base.model.Request;

/**
 * Репозиторий запросов на участие в событии
 */
public interface RequestRepository extends JpaRepository<Request, Long> {
}
