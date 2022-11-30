package ru.practicum.explorewithme.apiprivate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

/**
 * Валидация времени начала события (минимум +2 часа от текущего)
 */
public class EventDateValidator implements ConstraintValidator<EventDateCorrect, LocalDateTime> {
    @Override
    public void initialize(EventDateCorrect eventDateConstraint) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        return value.isAfter(LocalDateTime.now().plusHours(2));
    }
}
