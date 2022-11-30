package ru.practicum.explorewithme.base.model;

import ru.practicum.explorewithme.base.exception.WrongStateException;

/**
 * Типы локаций
 */
public enum LocationType {
    COUNTRY, //Страна
    STATE, //Регион
    CITY, //Город
    ADDRESS, //Адрес
    PLACE;//Неопределенное место заданного радиуса

    public static LocationType from(String type) {
        String upperType = type.toUpperCase();
        for (LocationType locationType : LocationType.values()) {
            if (locationType.toString().equals(upperType)) {
                return locationType;
            }
        }
        throw new WrongStateException(String.format("Location type %s is not found", type));
    }
}
