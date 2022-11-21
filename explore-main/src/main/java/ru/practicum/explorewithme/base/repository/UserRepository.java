package ru.practicum.explorewithme.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.base.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Репозиторий пользователей
 */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdIn(Collection<Long> ids);
}
