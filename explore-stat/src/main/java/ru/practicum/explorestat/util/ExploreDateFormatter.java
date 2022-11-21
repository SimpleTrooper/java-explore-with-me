package ru.practicum.explorestat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Утилитарный класс для форматирования даты
 */
@Component
public class ExploreDateFormatter {
    private static DateTimeFormatter dateTimeFormatter;

    @Value("${spring.mvc.format.date-time}")
    public void setDateTimeFormatter(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(dateTimeFormatter);
    }

    public static LocalDateTime toLocalDateTime(String localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return LocalDateTime.parse(localDateTime, dateTimeFormatter);
    }
}
