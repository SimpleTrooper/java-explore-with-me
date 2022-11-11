package ru.practicum.explorewithme.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.base.model.Compilation;

/**
 * Репозиторий подборок событий
 */
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
