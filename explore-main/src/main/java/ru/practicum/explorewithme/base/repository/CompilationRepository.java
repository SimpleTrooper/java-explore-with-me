package ru.practicum.explorewithme.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.base.model.Compilation;

import java.util.Optional;

/**
 * Репозиторий подборок событий
 */
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @EntityGraph(value = "compilations-events-graph")
    Optional<Compilation> findById(Long id);

    @EntityGraph(value = "compilations-events-graph")
    Page<Compilation> findAll(Pageable pageable);

    @EntityGraph(value = "compilations-events-graph")
    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
