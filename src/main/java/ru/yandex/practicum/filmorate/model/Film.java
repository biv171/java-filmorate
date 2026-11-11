package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {
    Long id;
    String name = "";
    String description = "";
    LocalDate releaseDate = LocalDate.of(1000,10,10);
    Integer duration = 0;
    Set<Long> smiles = new HashSet<>();

    public int countOfSmiles() {
        return getSmiles().size();
    }

    @Override
    public int compareTo(Film film) {
        return Integer.compare(countOfSmiles(), film.countOfSmiles());
    }
}