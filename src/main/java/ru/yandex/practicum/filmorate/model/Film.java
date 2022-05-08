package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private Long id;

    @NotEmpty(message = "Название фильма не должно быть пустым")
    private String name;

    @NotBlank
    @Size(max = 200, message = "Длинна описания должна быть не более 200 символов")
    private String description;

    @Past
    private LocalDate releaseDate;

    private Duration duration;
}
