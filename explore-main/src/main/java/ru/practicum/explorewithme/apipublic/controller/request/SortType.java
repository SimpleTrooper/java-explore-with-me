package ru.practicum.explorewithme.apipublic.controller.request;

import ru.practicum.explorewithme.base.exception.RequestNotFoundException;

/**
 * Варианты сортировки для запроса
 */
public enum SortType {
    EVENT_DATE, //по дате события
    VIEWS; //по количеству просмотров

    public static SortType from(String value) {
        if (value == null) {
            return null;
        }
        String upperCase = value.toUpperCase();
        for (SortType type: SortType.values()) {
            if (type.toString().equals(upperCase)) {
                return type;
            }
        }

        throw new RequestNotFoundException(String.format("Sorting type %s is not found", value));
    }
}
