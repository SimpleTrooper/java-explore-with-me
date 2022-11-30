package ru.practicum.explorewithme.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.base.model.Category;

/**
 * Репозиторий категорий
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
