package ru.practicum.explorewithme.base.model;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import ru.practicum.explorewithme.base.exception.WrongStateException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(EventState.class)
class EventStateTest {
    @Test
    void shouldThrowWrongStateForUNKNOWN() {
        assertThrows(WrongStateException.class, () -> EventState.from("UNKNOWN"));
    }
}