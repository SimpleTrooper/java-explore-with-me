package ru.practicum.explorewithme.admin.service;

import ru.practicum.explorewithme.admin.dto.AdminLocationDto;
import ru.practicum.explorewithme.admin.dto.NewLocationDto;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import java.util.List;

/**
 * Бизнес-логика локаций
 */
public interface AdminLocationService {

    /**
     * Получение всех локаций с фильтрацией по id и пагинацией (если отсутствует список id)
     *
     * @param locationIds       id нужных событий
     * @param paginationRequest пагинация
     * @return список DTO полученных локаций
     */
    List<AdminLocationDto> findAll(List<Long> locationIds, PaginationRequest paginationRequest);

    /**
     * Добавление новой локации
     *
     * @param newLocationDto данные новой локации
     * @return DTO добавленной локации
     */
    AdminLocationDto add(NewLocationDto newLocationDto);

    /**
     * Обновление параметров локации
     *
     * @param updatedLocationDto данные для обновления
     * @return DTO обновленной локации
     */
    AdminLocationDto update(NewLocationDto updatedLocationDto);

    /**
     * Удаление локации по id
     *
     * @param locationId id удаляемой локации
     */
    void delete(Long locationId);

    /**
     * Получить/обновить информацию о локации с сервиса геокодирования
     *
     * @param locationId id локации
     * @return DTO локации с обновленной информацией
     */
    AdminLocationDto resolveById(Long locationId);
}
