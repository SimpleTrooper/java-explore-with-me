package ru.practicum.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Утилитарный класс для форматирования даты
 */
@Component
@PropertySource(value = "classpath:util.properties")
public class ExploreDateFormatter {
    private static DateTimeFormatter dateTimeFormatter;

    @Value("${spring.mvc.format.date-time}")
    public void setDateTimeFormatter(String pattern) {
        if (pattern == null) {
            dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        if (dateTimeFormatter == null) {
            dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
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
