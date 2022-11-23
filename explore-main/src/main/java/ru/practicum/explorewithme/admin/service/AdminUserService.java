package ru.practicum.explorewithme.admin.service;

import ru.practicum.explorewithme.admin.dto.UserDto;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Бизнес-логика пользователей API администратора
 */
public interface AdminUserService {
    /**
     * Получение информации о пользователях
     *
     * @param ids               список id пользователей, если присутствует - пагинация игнорируется
     * @param paginationRequest пагинация
     * @return список DTO пользователей
     */
    List<UserDto> findAll(List<Long> ids, PaginationRequest paginationRequest);

    /**
     * Добавление нового пользователя
     *
     * @param newUser DTO для добавления
     * @return DTO добавленного пользователя
     */
    UserDto add(UserDto newUser);

    /**
     * Удаление пользователя
     *
     * @param userId id удаляемого пользователя
     */
    void delete(Long userId);
}
