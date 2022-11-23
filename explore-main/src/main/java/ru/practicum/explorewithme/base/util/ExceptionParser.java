package ru.practicum.explorewithme.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Класс для работы с исключениями
 */
public class ExceptionParser {

    /**
     * Получение строки из StackTrace
     * @param exception исключение
     * @return строка из StackTrace
     */
    public static String makeStringFromStackTrace(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
