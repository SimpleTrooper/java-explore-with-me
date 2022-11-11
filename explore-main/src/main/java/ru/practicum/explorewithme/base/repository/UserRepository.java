package ru.practicum.explorewithme.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.base.model.User;

/**
 * Репозиторий пользователей
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
