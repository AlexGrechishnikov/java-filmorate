package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {

    @EqualsAndHashCode.Include
    private Long id;

    @NotEmpty(message = "Название фильма не должно быть пустым")
    private String name;

    @NotBlank
    @Size(max = 200, message = "Длинна описания должна быть не более 200 символов")
    private String description;

    @Past
    private LocalDate releaseDate;

    private Integer duration;

    private Set<Long> likesIds = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}