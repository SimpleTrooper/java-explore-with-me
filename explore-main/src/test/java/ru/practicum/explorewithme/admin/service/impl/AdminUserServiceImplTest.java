package ru.practicum.explorewithme.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.explorewithme.admin.dto.UserDto;
import ru.practicum.explorewithme.admin.service.AdminUserService;
import ru.practicum.explorewithme.base.exception.UserNotFoundException;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Интеграционные тесты AdminUserServiceImpl
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AdminUserServiceImpl.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AdminUserServiceImplTest {
    final UserRepository userRepository;
    final AdminUserService adminUserService;

    User user1;
    User user2;

    @BeforeEach
    void init() {
        user1 = new User(null, "User 1", "mail1@mail.com");
        user2 = new User(null, "User 2", "mail2@mail.com");
        userRepository.save(user1);
        userRepository.save(user2);
    }

    /**
     * Стандартное поведение findAll
     */
    @Test
    void shouldFindAll() {
        List<Long> ids = new ArrayList<>();
        ids.add(user2.getId());
        List<UserDto> expected = List.of(UserDto.from(user2));

        List<UserDto> actual = adminUserService.findAll(ids, new PaginationRequest(0, 10));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(expected.size()));
        assertThat(actual.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(actual.get(0).getName(), equalTo(expected.get(0).getName()));
        assertThat(actual.get(0).getEmail(), equalTo(expected.get(0).getEmail()));
    }

    /**
     * Стандартное поведение findAll, при ids = null
     */
    @Test
    void shouldFindAllWhereIdsIsNull() {
        List<UserDto> expected = List.of(UserDto.from(user1), UserDto.from(user2));

        List<UserDto> actual = adminUserService.findAll(null, new PaginationRequest(0, 10));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(expected.size()));
        assertThat(actual.get(0).getId(), equalTo(expected.get(0).getId()));
        assertThat(actual.get(0).getName(), equalTo(expected.get(0).getName()));
        assertThat(actual.get(0).getEmail(), equalTo(expected.get(0).getEmail()));
        assertThat(actual.get(1).getId(), equalTo(expected.get(1).getId()));
        assertThat(actual.get(1).getName(), equalTo(expected.get(1).getName()));
        assertThat(actual.get(1).getEmail(), equalTo(expected.get(1).getEmail()));
    }

    /**
     * Стандартное поведение add
     */
    @Test
    void shouldAdd() {
        UserDto newUser = new UserDto("New user", null, "New name");

        UserDto actual = adminUserService.add(newUser);

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), notNullValue());
        assertThat(actual.getName(), equalTo(newUser.getName()));
        assertThat(actual.getEmail(), equalTo(newUser.getEmail()));
    }

    /**
     * Стандартное поведение delete
     */
    @Test
    void shouldDelete() {
        Long user1Id = user1.getId();
        assertDoesNotThrow(() -> adminUserService.delete(user1Id));

        assertThrows(UserNotFoundException.class, () -> adminUserService.delete(user1Id));
    }
}