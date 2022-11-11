package ru.practicum.explorewithme.base.model;

/**
 * Статус запроса на участие в событии
 */
public enum RequestState {
    PENDING, //Ожидание подтверждения/отказа
    ACCEPTED, //Принято
    REJECTED //Отклонено
}
