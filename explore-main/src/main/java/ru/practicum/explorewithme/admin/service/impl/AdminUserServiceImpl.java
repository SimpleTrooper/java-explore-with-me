package ru.practicum.explorewithme.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.admin.dto.UserDto;
import ru.practicum.explorewithme.admin.service.AdminUserService;
import ru.practicum.explorewithme.base.exception.UserNotFoundException;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики пользователей
 */
@Service
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Autowired
    public AdminUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAll(List<Long> ids, PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.makeOffsetBasedByFieldAsc("id");
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable).stream()
                    .map(UserDto::from)
                    .collect(Collectors.toList());
        }
        return userRepository.findAllByIdIn(ids).stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto add(UserDto newUser) {
        User user = UserDto.toUser(newUser);
        return UserDto.from(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
             new UserNotFoundException(String.format("User with id=%d is not found", userId))
        );
        userRepository.deleteById(userId);
    }
}
