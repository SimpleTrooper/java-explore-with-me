package ru.practicum.explorewithme.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.base.model.Event;

/**
 * Репозиторий событий
 */
public interface EventRepository extends JpaRepository<Event, Long> {
}
