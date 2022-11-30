package ru.practicum.explorewithme.base.model;

import ru.practicum.explorewithme.base.exception.WrongStateException;

/**
 * Состояние события
 */
public enum EventState {
    PENDING, //Ожидание публикации
    PUBLISHED, //Событие опубликовано
    CANCELED; //Отмена публикации

    public static EventState from(String state) {
        String upperState = state.toUpperCase();
        for (EventState eventState : EventState.values()) {
            if (eventState.toString().equals(upperState)) {
                return eventState;
            }
        }
        throw new WrongStateException(String.format("State %s is not found", state));
    }
}
