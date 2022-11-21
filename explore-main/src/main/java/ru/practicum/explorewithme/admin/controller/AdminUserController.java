package ru.practicum.explorewithme.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.admin.dto.UserDto;
import ru.practicum.explorewithme.admin.service.AdminUserService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.validation.groups.OnCreate;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер API администратора - пользователи
 */
@Slf4j
@HandleExceptions
@RestController
@Validated
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * Получение информации о пользователях
     * @param ids список id пользователей, если присутствует - пагинация игнорируется
     * @param from пагинация - начиная с
     * @param size пагинация - количество записей
     * @return список DTO пользователей
     */
    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false) List<Long> ids,
                                 @RequestParam(defaultValue = "0") Integer from,
                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("Admin request to get users with ids={}, from={}, size={}", ids, from, size);
        return adminUserService.findAll(ids, new PaginationRequest(from, size));
    }

    /**
     * Добавление нового пользователя
     * @param userDto DTO для добавления
     * @return DTO добавленного пользователя
     */
    @PostMapping
    @Validated(value = OnCreate.class)
    public UserDto add(@RequestBody @Valid UserDto userDto) {
        log.info("Admin request to add new user: {}", userDto);
        return adminUserService.add(userDto);
    }

    /**
     * Удаление пользователя
     * @param userId id удаляемого пользователя
     */
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Admin request to delete user with id={}", userId);
        adminUserService.delete(userId);
    }
}
