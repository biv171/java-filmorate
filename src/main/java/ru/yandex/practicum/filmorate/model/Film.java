package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Film {
    Long id;
    String name = "";
    String description = "";
    LocalDate releaseDate = LocalDate.of(1000,10,10);
    Integer duration = 0;
}