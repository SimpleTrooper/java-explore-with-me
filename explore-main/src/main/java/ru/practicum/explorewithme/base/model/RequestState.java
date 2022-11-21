package ru.practicum.explorewithme.base.model;

/**
 * Статус запроса на участие в событии
 */
public enum RequestState {
    PENDING, //Ожидание подтверждения/отказа
    CONFIRMED, //Принято
    REJECTED, //Отклонено
    CANCELED //Запрос отменен пользователем
}
