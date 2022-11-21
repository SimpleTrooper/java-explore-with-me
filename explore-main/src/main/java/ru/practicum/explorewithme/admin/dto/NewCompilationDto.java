package ru.practicum.explorewithme.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Compilation;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * DTO добавления подборки событий
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    private String title;

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(null, newCompilationDto.title, newCompilationDto.pinned, null);
    }
}
